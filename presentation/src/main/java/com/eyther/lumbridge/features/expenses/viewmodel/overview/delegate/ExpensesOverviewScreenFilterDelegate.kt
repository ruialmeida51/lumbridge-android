package com.eyther.lumbridge.features.expenses.viewmodel.overview.delegate

import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewFilter
import com.eyther.lumbridge.model.expenses.ExpensesMonthUi
import javax.inject.Inject

class ExpensesOverviewScreenFilterDelegate @Inject constructor() : IExpensesOverviewScreenFilterDelegate {

    override fun applyFilter(expenses: List<ExpensesMonthUi>, filter: ExpensesOverviewFilter): List<ExpensesMonthUi> {
        return when (filter) {
            is ExpensesOverviewFilter.DateRange -> expenses.applyDateRangeFilter(filter)
            is ExpensesOverviewFilter.UpTo -> expenses.applyUpToFilter(filter)
            is ExpensesOverviewFilter.StartingFrom -> expenses.applyStartingFromFilter(filter)
            is ExpensesOverviewFilter.None -> expenses
        }
    }

    private fun List<ExpensesMonthUi>.applyDateRangeFilter(dateRangeFilter: ExpensesOverviewFilter.DateRange): List<ExpensesMonthUi> {
        return filter { expense ->
            val startYear = dateRangeFilter.startYear.value
            val startMonth = dateRangeFilter.startMonth.value
            val endYear = dateRangeFilter.endYear.value
            val endMonth = dateRangeFilter.endMonth.value

            val expenseYear = expense.year.value
            val expenseMonth = expense.month.value

            (expenseYear > startYear || (expenseYear == startYear && expenseMonth >= startMonth)) &&
                (expenseYear < endYear || (expenseYear == endYear && expenseMonth <= endMonth))
        }
    }

    private fun List<ExpensesMonthUi>.applyUpToFilter(upToDateFilter: ExpensesOverviewFilter.UpTo): List<ExpensesMonthUi> {
        return filter { expense ->
            expense.year.value <= upToDateFilter.year.value &&
                expense.month.value <= upToDateFilter.month.value
        }
    }

    private fun List<ExpensesMonthUi>.applyStartingFromFilter(startingFromFilter: ExpensesOverviewFilter.StartingFrom): List<ExpensesMonthUi> {
        return filter { expense ->
            expense.year.value >= startingFromFilter.year.value &&
                expense.month.value >= startingFromFilter.month.value
        }
    }
}
