package com.eyther.lumbridge.features.expenses.viewmodel.edit.delegate

import com.eyther.lumbridge.features.expenses.model.edit.ExpensesEditScreenInputState
import kotlinx.coroutines.flow.StateFlow

interface IExpensesEditScreenInputHandler {
    val inputState: StateFlow<ExpensesEditScreenInputState>

    /**
     * Methods called on different input changes. Each one updates the input state with
     * the new value and performs any necessary validation.
     */
    fun onExpenseNameChanged(expenseName: String?)
    fun onExpenseAmountChanged(expenseAmount: Float?)
    fun onTypeChanged(typeOrdinal: Int?)
    fun onAllocationTypeChanged(allocationTypeOrdinal: Int?)
    fun onDateChanged(expenseDate: Long?)
    fun onSurplusOrExpenseChanged(choiceOrdinal: Int)

    /**
     * Validates the entire input state and returns an error message if the input state is invalid.
     * @return an error message if the percentages are invalid, null otherwise.
     * @see ExpensesEditScreenInputState
     */
    fun validateInput(inputState: ExpensesEditScreenInputState): Boolean

    /**
     * Checks if we have enough information available to enable the button.
     * @return true if we have enough information to enable the button, false otherwise.
     * @see ExpensesEditScreenInputState
     */
    fun shouldEnableSaveButton(inputState: ExpensesEditScreenInputState): Boolean

    /**
     * Helper function to update the input state.
     * @param update the lambda to update the input state.
     * @see ExpensesEditScreenInputState
     */
    fun updateInput(update: (ExpensesEditScreenInputState) -> ExpensesEditScreenInputState)
}
