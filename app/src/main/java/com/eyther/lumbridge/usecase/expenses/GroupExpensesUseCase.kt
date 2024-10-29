package com.eyther.lumbridge.usecase.expenses

import com.eyther.lumbridge.model.expenses.ExpenseUi
import com.eyther.lumbridge.model.expenses.ExpensesCategoryUi
import com.eyther.lumbridge.model.expenses.ExpensesDetailedUi
import com.eyther.lumbridge.model.expenses.ExpensesMonthUi
import com.eyther.lumbridge.model.snapshotsalary.SnapshotNetSalaryUi
import com.eyther.lumbridge.ui.common.model.math.MathOperator
import com.eyther.lumbridge.usecase.snapshotsalary.GetMostRecentSnapshotSalaryForDateUseCase
import java.time.Year
import javax.inject.Inject

/**
 * From a list of expenses, group them by month and then by category,  respectively, and return the total amount spent in each category.
 *
 * @param getMostRecentSnapshotSalaryForDateUseCase Use case to get the most recent snapshot salary for a given date.
 */
class GroupExpensesUseCase @Inject constructor(
    private val getMostRecentSnapshotSalaryForDateUseCase: GetMostRecentSnapshotSalaryForDateUseCase
) {
    operator fun invoke(
        expenses: List<ExpenseUi>,
        snapshotNetSalaries: List<SnapshotNetSalaryUi>
    ) = expenses.createExpensesPerMonth(snapshotNetSalaries)

    private fun List<ExpenseUi>.createExpensesPerMonth(
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

                ExpensesMonthUi(
                    month = yearMonth.second,
                    year = Year.of(yearMonth.first),
                    spent = spent,
                    expanded = false,
                    remainder = snapshotSalary - spent + gained,
                    snapshotMonthlyNetSalary = snapshotSalary,
                    categoryExpenses = expenses.toCategoryExpenses()
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
                    expensesDetailedUi = expenses.toDetailedExpense()
                )
            }
            .sortedBy { it.categoryType.orderOfAppearance }

    private fun List<ExpenseUi>.toDetailedExpense() = map {
        ExpensesDetailedUi(
            id = it.id,
            date = it.date,
            expenseAmount = it.expenseAmount,
            expenseName = it.expenseName
        )
    }
}
