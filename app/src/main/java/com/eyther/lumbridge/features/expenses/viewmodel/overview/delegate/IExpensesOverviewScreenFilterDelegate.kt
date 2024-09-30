package com.eyther.lumbridge.features.expenses.viewmodel.overview.delegate

import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewFilter
import com.eyther.lumbridge.model.expenses.ExpensesMonthUi

interface IExpensesOverviewScreenFilterDelegate {

    /**
     * Apply the filter to the expenses, returning only the ones that match the filter.
     *
     * @param expenses The expenses to be filtered
     * @param filter The filter to be applied
     *
     * @return The expenses that match the filter
     */
    fun applyFilter(expenses: List<ExpensesMonthUi>, filter: ExpensesOverviewFilter): List<ExpensesMonthUi>
}
