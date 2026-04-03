package com.eyther.lumbridge.domain.usecase.expenses

import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryDomain
import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryTypes
import com.eyther.lumbridge.domain.model.expenses.ExpenseDomain
import com.eyther.lumbridge.domain.model.expenses.ExpensesMonthDomain
import com.eyther.lumbridge.domain.model.expenses.MonthAllocationDomain
import com.eyther.lumbridge.domain.model.netsalary.allocation.MoneyAllocation
import com.eyther.lumbridge.domain.model.netsalary.allocation.MoneyAllocationType
import com.eyther.lumbridge.domain.model.snapshotsalary.SnapshotNetSalaryDomain
import com.eyther.lumbridge.domain.usecase.snapshotsalary.GetMostRecentSnapshotSalaryForDateUseCase
import com.eyther.lumbridge.shared.di.model.Schedulers
import kotlinx.coroutines.withContext
import java.time.Month
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
    ): List<ExpensesMonthDomain> = expenses.createExpensesPerMonth(
        showAllocationsOnExpenses = showAllocationsOnExpenses,
        shouldAddFoodCardToNecessitiesAllocation = shouldAddFoodCardToNecessitiesAllocation,
        snapshotNetSalaries = snapshotNetSalaries
    )

    private suspend fun List<ExpenseDomain>.createExpensesPerMonth(
        showAllocationsOnExpenses: Boolean,
        shouldAddFoodCardToNecessitiesAllocation: Boolean,
        snapshotNetSalaries: List<SnapshotNetSalaryDomain>
    ): List<ExpensesMonthDomain> {
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

                ExpensesMonthDomain(
                    month = Month.of(yearMonth.second),
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

    private fun List<ExpenseDomain>.toCategoryExpenses(): List<ExpensesCategoryDomain> =
        groupBy { it.categoryType }
            .map { (type, expenses) ->
                val categoryExpenseSpent = expenses.sumOf { it.expenseAmount.toDouble() }.toFloat()

                ExpensesCategoryDomain(
                    categoryType = type,
                    spent = categoryExpenseSpent,
                    expenses = expenses
                )
            }

    private suspend fun getMoneyAllocations(
        snapshotAllocations: List<MoneyAllocation>,
        expensesByCategory: List<ExpensesCategoryDomain>,
        foodCardAmount: Float,
        shouldAddFoodCardToNecessitiesAllocation: Boolean
    ): List<MonthAllocationDomain> = withContext(schedulers.cpu) {
        val allocations = mutableListOf<MonthAllocationDomain>()

        val detailedExpensesByAllocation = expensesByCategory
            .filter { it.categoryType !is ExpensesCategoryTypes.Surplus }
            .flatMap { it.expenses }
            .groupBy { it.allocation }

        val detailedGainByAllocation = expensesByCategory
            .filter { it.categoryType is ExpensesCategoryTypes.Surplus }
            .flatMap { it.expenses }
            .groupBy { it.allocation }

        snapshotAllocations.forEach { allocation ->
            val addFoodCardToNecessities = allocation.type is MoneyAllocationType.Necessities && shouldAddFoodCardToNecessitiesAllocation

            val spentForAllocationType = detailedExpensesByAllocation.entries
                .find { it.key == allocation.type }
                ?.value
                ?.sumOf { it.expenseAmount.toInt() }
                ?.toFloat()

            val gainedForAllocationType = detailedGainByAllocation.entries
                .find { it.key == allocation.type }
                ?.value
                ?.sumOf { it.expenseAmount.toInt() }
                ?.toFloat()

            allocations.add(
                MonthAllocationDomain(
                    type = allocation.type,
                    allocated = allocation.amount,
                    spent = spentForAllocationType ?: 0f,
                    gained = (gainedForAllocationType ?: 0f) + if (addFoodCardToNecessities) foodCardAmount else 0f
                )
            )
        }

        return@withContext allocations.sortedBy { it.type.ordinal }
    }
}
