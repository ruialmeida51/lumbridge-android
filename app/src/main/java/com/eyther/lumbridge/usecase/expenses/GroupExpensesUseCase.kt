package com.eyther.lumbridge.usecase.expenses

import com.eyther.lumbridge.model.expenses.ExpenseUi
import com.eyther.lumbridge.model.expenses.ExpensesCategoryUi
import com.eyther.lumbridge.model.expenses.ExpensesDetailedUi
import com.eyther.lumbridge.model.expenses.ExpensesMonthAllocationUi
import com.eyther.lumbridge.model.expenses.ExpensesMonthUi
import com.eyther.lumbridge.model.finance.MoneyAllocationTypeUi
import com.eyther.lumbridge.model.snapshotsalary.SnapshotNetSalaryUi
import com.eyther.lumbridge.shared.di.model.Schedulers
import com.eyther.lumbridge.ui.common.model.math.MathOperator
import com.eyther.lumbridge.usecase.snapshotsalary.GetMostRecentSnapshotSalaryForDateUseCase
import kotlinx.coroutines.withContext
import java.time.Year
import javax.inject.Inject

/**
 * From a list of expenses, group them by month and then by category,  respectively, and return the total amount spent in each category.
 *
 * @param getMostRecentSnapshotSalaryForDateUseCase Use case to get the most recent snapshot salary for a given date.
 */
class GroupExpensesUseCase @Inject constructor(
    private val getMostRecentSnapshotSalaryForDateUseCase: GetMostRecentSnapshotSalaryForDateUseCase,
    private val schedulers: Schedulers
) {
    suspend operator fun invoke(
        expenses: List<ExpenseUi>,
        snapshotNetSalaries: List<SnapshotNetSalaryUi>,
        showAllocationsOnExpenses: Boolean
    ) = expenses.createExpensesPerMonth(showAllocationsOnExpenses, snapshotNetSalaries)

    private suspend fun List<ExpenseUi>.createExpensesPerMonth(
        showAllocationsOnExpenses: Boolean,
        snapshotNetSalaries: List<SnapshotNetSalaryUi>
    ): List<ExpensesMonthUi> {
        return groupBy { it.date.year to it.date.month }
            .map { (yearMonth, expenses) ->
                val spent = expenses
                    .filter { it.categoryType.operator == MathOperator.SUBTRACTION }
                    .sumOf { it.expenseAmount.toDouble() }.toFloat()

                val gained = expenses
                    .filter { it.categoryType.operator == MathOperator.ADDITION }
                    .sumOf { it.expenseAmount.toDouble() }.toFloat()

                val snapshotSalary = getMostRecentSnapshotSalaryForDateUseCase(
                    snapshotNetSalaries = snapshotNetSalaries,
                    year = yearMonth.first,
                    month = yearMonth.second.value
                )

                val snapshotNetSalary = snapshotSalary?.netSalary ?: 0f
                val snapshotAllocations = snapshotSalary?.moneyAllocations ?: emptyList()
                val expensesByCategory = expenses.toCategoryExpenses()

                ExpensesMonthUi(
                    month = yearMonth.second,
                    year = Year.of(yearMonth.first),
                    spent = spent,
                    gained = gained,
                    expanded = false,
                    remainder = snapshotNetSalary - spent + gained,
                    snapshotMonthlyNetSalary = snapshotNetSalary,
                    snapshotAllocations = if (showAllocationsOnExpenses) {
                        getMoneyAllocations(snapshotAllocations, expensesByCategory)
                    } else {
                        emptyList()
                    },
                    categoryExpenses = expensesByCategory
                )
            }
    }

    private fun List<ExpenseUi>.toCategoryExpenses() =
        groupBy { it.categoryType }
            .map { (type, expenses) ->
                val categoryExpenseSpent = expenses.sumOf { it.expenseAmount.toDouble() }.toFloat()

                ExpensesCategoryUi(
                    categoryType = type,
                    spent = categoryExpenseSpent,
                    expensesDetailedUi = expenses.toDetailedExpense(),
                )
            }
            .sortedBy { it.categoryType.orderOfAppearance }

    private fun List<ExpenseUi>.toDetailedExpense() = map {
        ExpensesDetailedUi(
            id = it.id,
            date = it.date,
            expenseAmount = it.expenseAmount,
            expenseName = it.expenseName,
            allocationTypeUi = it.allocationTypeUi
        )
    }

    private suspend fun getMoneyAllocations(
        snapshotAllocations: List<MoneyAllocationTypeUi>,
        expensesByCategory: List<ExpensesCategoryUi>
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

        snapshotAllocations.forEach { allocationType ->
            val spentForAllocationType = detailedExpensesByAllocation.entries
                .find { it.key.ordinal == allocationType.ordinal }
                ?.value
                ?.sumOf { it.expenseAmount.toInt() } // Convert to int to speed up calculations
                ?.toFloat()

            val gainedForAllocationType = detailedGainByAllocation.entries
                .find { it.key.ordinal == allocationType.ordinal }
                ?.value
                ?.sumOf { it.expenseAmount.toInt() } // Convert to int to speed up calculations
                ?.toFloat()

            allocations.add(
                ExpensesMonthAllocationUi(
                    type = allocationType,
                    spent = spentForAllocationType ?: 0f,
                    gained = gainedForAllocationType ?: 0f
                )
            )
        }

        return@withContext allocations.sortedBy { it.type.ordinal }
    }
}
