package com.eyther.lumbridge.features.expenses.viewmodel.overview.delegate

import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewSortBy
import com.eyther.lumbridge.model.expenses.ExpensesMonthUi
import javax.inject.Inject

class ExpensesOverviewScreenSortByDelegate @Inject constructor(): IExpensesOverviewScreenSortByDelegate {

    override fun applySortBy(expenses: List<ExpensesMonthUi>, sortBy: ExpensesOverviewSortBy): List<ExpensesMonthUi> {
        return when(sortBy) {
            ExpensesOverviewSortBy.SpentAscending -> expenses.applyAscendingByAmount()
            ExpensesOverviewSortBy.SpentDescending -> expenses.applyDescendingByAmount()
            ExpensesOverviewSortBy.DateAscending -> expenses.applyAscendingByDate()
            ExpensesOverviewSortBy.DateDescending -> expenses.applyDescendingByDate()
        }
    }

    private fun List<ExpensesMonthUi>.applyAscendingByAmount(): List<ExpensesMonthUi> {
        return sortedBy { it.spent }
    }

    private fun List<ExpensesMonthUi>.applyDescendingByAmount(): List<ExpensesMonthUi> {
        return sortedByDescending { it.spent }
    }

    private fun List<ExpensesMonthUi>.applyAscendingByDate(): List<ExpensesMonthUi> {
        return sortedWith(compareBy({ it.year.value }, { it.month.value }))
    }

    private fun List<ExpensesMonthUi>.applyDescendingByDate(): List<ExpensesMonthUi> {
        return sortedWith(compareBy({ it.year.value }, { it.month.value })).reversed()
    }
}
