package com.eyther.lumbridge.features.expenses.viewmodel.overview.delegate

import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewSortBy
import com.eyther.lumbridge.model.expenses.ExpensesMonthUi

interface IExpensesOverviewScreenSortByDelegate {
    /**
     * Apply the sort by to the expenses, returning the expenses sorted by the sort by.
     *
     * @param expenses The expenses to be sorted
     * @param sortBy The sort by to be applied
     *
     * @return The expenses sorted
     */
    fun applySortBy(expenses: List<ExpensesMonthUi>, sortBy: ExpensesOverviewSortBy): List<ExpensesMonthUi>
}
