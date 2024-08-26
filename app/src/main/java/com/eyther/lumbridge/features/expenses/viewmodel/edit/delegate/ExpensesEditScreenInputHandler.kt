package com.eyther.lumbridge.features.expenses.viewmodel.edit.delegate

import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.getErrorOrNull
import com.eyther.lumbridge.features.expenses.model.edit.ExpensesEditScreenInputState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ExpensesEditScreenInputHandler @Inject constructor() : IExpensesEditScreenInputHandler {
    override val inputState = MutableStateFlow(ExpensesEditScreenInputState())

    override fun onExpenseNameChanged(expenseName: String?) {
        updateInput { state ->
            val expenseNameText = expenseName ?: ""

            state.copy(
                expenseName = state.expenseName.copy(
                    text = expenseNameText,
                    error = expenseNameText.getErrorOrNull(
                        R.string.expenses_invalid_name
                    )
                )
            )
        }
    }

    override fun onExpenseAmountChanged(expenseAmount: Float?) {
        updateInput { state ->

            // If the expense amount is less than or equal to 0, it's invalid.
            val expenseAmountText = if ((expenseAmount ?: 0f) <= 0f) {
                null
            } else {
                expenseAmount?.toString()
            }

            state.copy(
                expenseAmount = state.expenseAmount.copy(
                    text = expenseAmountText,
                    error = expenseAmountText.getErrorOrNull(
                        R.string.expenses_invalid_amount
                    )
                )
            )
        }
    }

    override fun validateInput(inputState: ExpensesEditScreenInputState): Boolean {
        return inputState.expenseName.isValid() &&
            inputState.expenseAmount.isValid()
    }

    override fun shouldEnableSaveButton(inputState: ExpensesEditScreenInputState): Boolean {
        return validateInput(inputState)
    }

    override fun updateInput(update: (ExpensesEditScreenInputState) -> ExpensesEditScreenInputState) {
        inputState.update { currentState ->
            update(currentState)
        }
    }

}
