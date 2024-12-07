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
        expenses: List<ExpenseUi>,
        addFoodCardToNecessitiesAllocation: Boolean
    ) = withContext(schedulers.cpu) {
        return@withContext getBalanceSheetNet(
            currentNetSalary = currentNetSalary,
            snapshotSalaries = snapshotSalaries,
            expenses = expenses,
            addFoodCardToNecessitiesAllocation = addFoodCardToNecessitiesAllocation
        )
    }

    private fun getBalanceSheetNet(
        currentNetSalary: Float?,
        snapshotSalaries: List<SnapshotNetSalaryUi>,
        expenses: List<ExpenseUi>,
        addFoodCardToNecessitiesAllocation: Boolean
    ): BalanceSheetNetUi? {
        if (expenses.isEmpty() || currentNetSalary == null) return null

        val (moneyIn, moneyOut) = expenses
            .groupBy { it.date.year to it.date.month }
            .map { (yearMonth, expenses) ->
                val spent = expenses
                    .filter { it.categoryType.operator == MathOperator.SUBTRACTION }
                    .sumOf { it.expenseAmount.toDouble() }.toFloat()

                val gained = expenses
                    .filter { it.categoryType.operator == MathOperator.ADDITION }
                    .sumOf { it.expenseAmount.toDouble() }.toFloat()

                val snapshotSalary = getMostRecentSnapshotSalaryForDateUseCase(
                    snapshotNetSalaries = snapshotSalaries,
                    year = yearMonth.first,
                    month = yearMonth.second.value
                )

                val snapshotNetSalary = snapshotSalary?.netSalary ?: 0f
                val foodCardAmount = snapshotSalary?.foodCardAmount ?: 0f
                val income = snapshotNetSalary + gained + if (addFoodCardToNecessitiesAllocation) foodCardAmount else 0f

                return@map income to spent
            }.reduce { acc, (moneyIn, moneyOut) ->
                acc.copy(
                    first = acc.first + moneyIn,
                    second = acc.second + moneyOut
                )
            }

        return BalanceSheetNetUi(
            moneyIn = moneyIn.toFloat(),
            moneyOut = moneyOut,
            net = (moneyIn - moneyOut)
        )
    }
}
