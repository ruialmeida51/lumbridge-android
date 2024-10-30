package com.eyther.lumbridge.features.expenses.viewmodel.add.delegate

import com.eyther.lumbridge.features.expenses.model.add.ExpensesAddScreenInputState
import kotlinx.coroutines.flow.StateFlow

interface IExpensesAddScreenInputHandler {
    val inputState: StateFlow<ExpensesAddScreenInputState>

    /**
     * Methods called on different input changes. Each one updates the input state with
     * the new value and performs any necessary validation.
     */
    fun onNameChanged(expenseName: String?)
    fun onAmountChanged(expenseAmount: Float?)
    fun onDateChanged(expenseDate: Long?)
    fun onTypeChanged(typeOrdinal: Int?)
    fun onSurplusOrExpenseChanged(choiceOrdinal: Int)

    /**
     * Validates the entire input state and returns an error message if the input state is invalid.
     * @return an error message if the percentages are invalid, null otherwise.
     * @see ExpensesAddScreenInputState
     */
    fun validateInput(inputState: ExpensesAddScreenInputState): Boolean

    /**
     * Checks if we have enough information available to enable the button.
     * @return true if we have enough information to enable the button, false otherwise.
     * @see ExpensesAddScreenInputState
     */
    fun shouldEnableSaveButton(inputState: ExpensesAddScreenInputState): Boolean

    /**
     * Helper function to update the input state.
     * @param update the lambda to update the input state.
     * @see ExpensesAddScreenInputState
     */
    fun updateInput(update: (ExpensesAddScreenInputState) -> ExpensesAddScreenInputState)
}
