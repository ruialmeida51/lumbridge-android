package com.eyther.lumbridge.features.expenses.viewmodel.monthdetails

import com.eyther.lumbridge.features.expenses.model.monthdetails.ExpensesMonthDetailScreenViewEffect
import com.eyther.lumbridge.features.expenses.model.monthdetails.ExpensesMonthDetailScreenViewState
import com.eyther.lumbridge.model.expenses.ExpensesCategoryUi
import com.eyther.lumbridge.model.expenses.ExpensesMonthUi
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IExpensesMonthDetailScreenViewModel {
    val viewState: StateFlow<ExpensesMonthDetailScreenViewState>
    val viewEffects: SharedFlow<ExpensesMonthDetailScreenViewEffect>

    /**
     * Removes the given expense from the list of expenses.
     *
     * @param expensesMonth The expense to remove
     */
    fun onDeleteExpense(expensesMonth: ExpensesMonthUi)

    /**
     * Expand the given category inside the selected month. This will show a detailed view of the category's expenses.
     * If the category is already expanded, it will collapse it.
     * If there is another category already expanded, it will NOT collapse it.
     *
     * @param category The category to expand
     */
    fun expandCategory(category: ExpensesCategoryUi)

    /**
     * Collapses all the views.
     */
    fun collapseAll()

    /**
     * Expands all the views.
     */
    fun expandAll()
}