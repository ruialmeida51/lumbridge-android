package com.eyther.lumbridge.usecase.expenses

import com.eyther.lumbridge.domain.model.expenses.ExpenseDomain
import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryTypes
import com.eyther.lumbridge.domain.model.netsalary.allocation.MoneyAllocation
import com.eyther.lumbridge.domain.model.netsalary.allocation.MoneyAllocationType
import com.eyther.lumbridge.domain.model.snapshotsalary.SnapshotNetSalaryDomain
import com.eyther.lumbridge.mapper.expenses.toUi
import com.eyther.lumbridge.mapper.finance.toUi
import com.eyther.lumbridge.model.expenses.ExpensesCategoryUi
import com.eyther.lumbridge.model.expenses.ExpensesDetailedUi
import com.eyther.lumbridge.model.expenses.ExpensesMonthAllocationUi
import com.eyther.lumbridge.model.expenses.ExpensesMonthUi
import com.eyther.lumbridge.shared.di.model.Schedulers
import com.eyther.lumbridge.ui.common.model.math.MathOperator
import com.eyther.lumbridge.usecase.snapshotsalary.GetMostRecentSnapshotSalaryForDateUseCase
import kotlinx.coroutines.withContext
import java.time.Year
import javax.inject.Inject

/**
 * From a list of expenses, group them by month and then by category, respectively, and return the total amount spent in each category.
 *
 * @param getMostRecentSnapshotSalaryForDateUseCase Use case to get the most recent snapshot salary for a given date.
 */
class GroupExpensesUseCase @Inject constructor(
    private val getMostRecentSnapshotSalaryForDateUseCase: GetMostRecentSnapshotSalaryForDateUseCase,
    private val schedulers: Schedulers
) {
    suspend operator fun invoke(
        expenses: List<ExpenseDomain>,
        snapshotNetSalaries: List<SnapshotNetSalaryDomain>,
        showAllocationsOnExpenses: Boolean,
        shouldAddFoodCardToNecessitiesAllocation: Boolean
    ): List<ExpensesMonthUi> = expenses.createExpensesPerMonth(
        showAllocationsOnExpenses = showAllocationsOnExpenses,
        shouldAddFoodCardToNecessitiesAllocation = shouldAddFoodCardToNecessitiesAllocation,
        snapshotNetSalaries = snapshotNetSalaries
    )

    private suspend fun List<ExpenseDomain>.createExpensesPerMonth(
        showAllocationsOnExpenses: Boolean,
        shouldAddFoodCardToNecessitiesAllocation: Boolean,
        snapshotNetSalaries: List<SnapshotNetSalaryDomain>
    ): List<ExpensesMonthUi> {
        return groupBy { it.date.year to it.date.monthValue }
            .map { (yearMonth, expenses) ->
                val spent = expenses
                    .filter { it.categoryType !is ExpensesCategoryTypes.Surplus }
                    .sumOf { it.expenseAmount.toDouble() }.toFloat()

                val gained = expenses
                    .filter { it.categoryType is ExpensesCategoryTypes.Surplus }
                    .sumOf { it.expenseAmount.toDouble() }.toFloat()

                val snapshotSalary = getMostRecentSnapshotSalaryForDateUseCase(
                    snapshotNetSalaries = snapshotNetSalaries,
                    year = yearMonth.first,
                    month = yearMonth.second
                )

                val snapshotNetSalary = snapshotSalary?.netSalary ?: 0f
                val snapshotFoodCardAmount = snapshotSalary?.foodCardAmount ?: 0f
                val snapshotAllocations = snapshotSalary?.moneyAllocations ?: emptyList()
                val expensesByCategory = expenses.toCategoryExpenses()

                ExpensesMonthUi(
                    month = yearMonth.second.let { java.time.Month.of(it) },
                    year = Year.of(yearMonth.first),
                    spent = spent,
                    gained = gained,
                    remainder = snapshotNetSalary - spent + gained,
                    snapshotMonthlyNetSalary = snapshotNetSalary,
                    snapshotAllocations = if (showAllocationsOnExpenses) {
                        getMoneyAllocations(
                            snapshotAllocations = snapshotAllocations,
                            expensesByCategory = expensesByCategory,
                            foodCardAmount = snapshotFoodCardAmount,
                            shouldAddFoodCardToNecessitiesAllocation = shouldAddFoodCardToNecessitiesAllocation
                        )
                    } else {
                        emptyList()
                    },
                    categoryExpenses = expensesByCategory
                )
            }
    }

    private fun List<ExpenseDomain>.toCategoryExpenses() =
        groupBy { it.categoryType }
            .map { (type, expenses) ->
                val categoryExpenseSpent = expenses.sumOf { it.expenseAmount.toDouble() }.toFloat()

                ExpensesCategoryUi(
                    categoryType = type.toUi(),
                    spent = categoryExpenseSpent,
                    expensesDetailedUi = expenses.toDetailedExpense(),
                )
            }
            .sortedBy { it.categoryType.orderOfAppearance }

    private fun List<ExpenseDomain>.toDetailedExpense() = map {
        ExpensesDetailedUi(
            id = it.id,
            date = it.date,
            expenseAmount = it.expenseAmount,
            expenseName = it.expenseName,
            allocationTypeUi = it.allocation.toUi()
        )
    }

    private suspend fun getMoneyAllocations(
        snapshotAllocations: List<MoneyAllocation>,
        expensesByCategory: List<ExpensesCategoryUi>,
        foodCardAmount: Float,
        shouldAddFoodCardToNecessitiesAllocation: Boolean
    ): List<ExpensesMonthAllocationUi> = withContext(schedulers.cpu) {
        val allocations = mutableListOf<ExpensesMonthAllocationUi>()

        val detailedExpensesByAllocation = expensesByCategory
            .filter { it.categoryType.operator == MathOperator.SUBTRACTION }
            .flatMap { it.expensesDetailedUi }
            .groupBy { it.allocationTypeUi }

        val detailedGainByAllocation = expensesByCategory
            .filter { it.categoryType.operator == MathOperator.ADDITION }
            .flatMap { it.expensesDetailedUi }
            .groupBy { it.allocationTypeUi }

        snapshotAllocations.forEach { allocation ->
            val allocationTypeUi = allocation.toUi()
            val addFoodCardToNecessities = allocation.type is MoneyAllocationType.Necessities && shouldAddFoodCardToNecessitiesAllocation

            val spentForAllocationType = detailedExpensesByAllocation.entries
                .find { it.key.ordinal == allocationTypeUi.ordinal }
                ?.value
                ?.sumOf { it.expenseAmount.toInt() } // Convert to int to speed up calculations
                ?.toFloat()

            val gainedForAllocationType = detailedGainByAllocation.entries
                .find { it.key.ordinal == allocationTypeUi.ordinal }
                ?.value
                ?.sumOf { it.expenseAmount.toInt() } // Convert to int to speed up calculations
                ?.toFloat()

            allocations.add(
                ExpensesMonthAllocationUi(
                    type = allocationTypeUi,
                    spent = spentForAllocationType ?: 0f,
                    gained = (gainedForAllocationType ?: 0f) + if (addFoodCardToNecessities) foodCardAmount else 0f
                )
            )
        }

        return@withContext allocations.sortedBy { it.type.ordinal }
    }
}
