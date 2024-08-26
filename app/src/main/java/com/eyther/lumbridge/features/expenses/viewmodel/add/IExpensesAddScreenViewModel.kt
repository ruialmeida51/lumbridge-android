package com.eyther.lumbridge.features.expenses.viewmodel.add

import com.eyther.lumbridge.features.expenses.model.add.ExpensesAddScreenViewEffect
import com.eyther.lumbridge.features.expenses.model.add.ExpensesAddScreenViewState
import com.eyther.lumbridge.features.expenses.viewmodel.add.delegate.IExpensesAddScreenInputHandler
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IExpensesAddScreenViewModel : IExpensesAddScreenInputHandler {
    val viewState: StateFlow<ExpensesAddScreenViewState>
    val viewEffects: SharedFlow<ExpensesAddScreenViewEffect>

    /**
     * @return the max selectable year that an expense can be added for.
     */
    fun getMaxSelectableYear(): Int

    /**
     * @return the min selectable year that an expense can be added for.
     */
    fun getMinSelectableYear(): Int

    /**
     * Called when the user taps the save button, saves the expense to the database.
     */
    fun onAddExpense()
}
