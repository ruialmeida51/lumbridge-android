package com.eyther.lumbridge.usecase.expenses

import com.eyther.lumbridge.features.overview.breakdown.model.BalanceSheetNetUi
import com.eyther.lumbridge.model.expenses.ExpenseUi
import com.eyther.lumbridge.model.snapshotsalary.SnapshotNetSalaryUi
import com.eyther.lumbridge.shared.di.model.Schedulers
import com.eyther.lumbridge.ui.common.model.math.MathOperator
import com.eyther.lumbridge.usecase.snapshotsalary.GetMostRecentSnapshotSalaryForDateUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetBalanceSheetUseCase @Inject constructor(
    private val getMostRecentSnapshotSalaryForDateUseCase: GetMostRecentSnapshotSalaryForDateUseCase,
    private val schedulers: Schedulers
) {
    suspend operator fun invoke(
        currentNetSalary: Float?,
        snapshotSalaries: List<SnapshotNetSalaryUi>,
        expenses: List<ExpenseUi>
    ) = withContext(schedulers.cpu) {
        return@withContext getBalanceSheetNet(
            currentNetSalary = currentNetSalary,
            snapshotSalaries = snapshotSalaries,
            expenses = expenses
        )
    }

    private fun getBalanceSheetNet(
        currentNetSalary: Float?,
        snapshotSalaries: List<SnapshotNetSalaryUi>,
        expenses: List<ExpenseUi>
    ): BalanceSheetNetUi? {
        if (expenses.isEmpty() || currentNetSalary == null) return null

        val (moneyIn, moneyOut) = expenses
            .groupBy { it.date }
            .map { (date, expenses) ->
                val snapshotSalaryForDate = getMostRecentSnapshotSalaryForDateUseCase(
                    snapshotNetSalaries = snapshotSalaries,
                    year = date.year,
                    month = date.monthValue
                )

                val salaryForMonth = snapshotSalaryForDate?.netSalary ?: currentNetSalary

                val (expensesMoneyIn, expensesMoneyOut) = expenses.fold(0 to 0) { acc, expense ->
                    if (expense.categoryType.operator == MathOperator.SUBTRACTION) {
                        acc.copy(second = acc.second + expense.expenseAmount.toInt())
                    } else {
                        acc.copy(first = acc.first + expense.expenseAmount.toInt())
                    }
                }

                return@map (expensesMoneyIn + salaryForMonth).toInt() to expensesMoneyOut
            }.reduce { acc, (moneyIn, moneyOut) ->
                acc.copy(
                    first = acc.first + moneyIn,
                    second = acc.second + moneyOut
                )
            }

        return BalanceSheetNetUi(
            moneyIn = moneyIn.toFloat(),
            moneyOut = moneyOut.toFloat(),
            net = (moneyIn - moneyOut).toFloat()
        )
    }
}
