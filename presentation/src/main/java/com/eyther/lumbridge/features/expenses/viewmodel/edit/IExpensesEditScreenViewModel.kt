package com.eyther.lumbridge.features.expenses.viewmodel.edit

import com.eyther.lumbridge.features.expenses.model.edit.ExpensesEditScreenViewEffect
import com.eyther.lumbridge.features.expenses.model.edit.ExpensesEditScreenViewState
import com.eyther.lumbridge.features.expenses.viewmodel.edit.delegate.IExpensesEditScreenInputHandler
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IExpensesEditScreenViewModel : IExpensesEditScreenInputHandler {
    val viewState: StateFlow<ExpensesEditScreenViewState>
    val viewEffects: SharedFlow<ExpensesEditScreenViewEffect>

    /**
     * Delete an expense from a given category.
     */
    fun delete()

    /**
     * Save the expense with the given name and amount.
     */
    fun save()

    /**
     * @return the max selectable year that an expense can be added for.
     */
    fun getMaxSelectableYear(): Int

    /**
     * @return the min selectable year that an expense can be added for.
     */
    fun getMinSelectableYear(): Int
}
