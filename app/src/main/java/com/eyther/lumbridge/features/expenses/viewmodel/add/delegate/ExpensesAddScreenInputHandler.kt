package com.eyther.lumbridge.features.expenses.viewmodel.add.delegate

import com.eyther.lumbridge.R
import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryTypes
import com.eyther.lumbridge.domain.time.toLocalDate
import com.eyther.lumbridge.extensions.kotlin.getErrorOrNull
import com.eyther.lumbridge.features.expenses.model.add.ExpensesAddScreenInputState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ExpensesAddScreenInputHandler @Inject constructor() : IExpensesAddScreenInputHandler {
    override val inputState: MutableStateFlow<ExpensesAddScreenInputState> =
        MutableStateFlow(ExpensesAddScreenInputState())

    override fun onNameChanged(expenseName: String?) {
        updateInput { state ->
            val expenseNameText = expenseName ?: ""

            state.copy(
                nameInput = state.nameInput.copy(
                    text = expenseNameText,
                    error = expenseNameText.getErrorOrNull(
                        R.string.expenses_invalid_name
                    )
                )
            )
        }
    }

    override fun onAmountChanged(expenseAmount: Float?) {
        updateInput { state ->

            // If the expense amount is less than or equal to 0, it's invalid.
            val expenseAmountText = if ((expenseAmount ?: 0f) <= 0f) {
                null
            } else {
                expenseAmount?.toString()
            }

            state.copy(
                amountInput = state.amountInput.copy(
                    text = expenseAmountText,
                    error = expenseAmountText.getErrorOrNull(
                        R.string.expenses_invalid_amount
                    )
                )
            )
        }
    }

    override fun onDateChanged(expenseDate: Long?) {
        updateInput { state ->
            state.copy(
                dateInput = state.dateInput.copy(
                    date = expenseDate?.toLocalDate(),
                    error = expenseDate?.toString().getErrorOrNull(
                        R.string.expenses_invalid_date
                    )
                )
            )
        }
    }

    override fun onTypeChanged(typeOrdinal: Int?) {
        updateInput { state ->
            state.copy(
                categoryType = ExpensesCategoryTypes.of(typeOrdinal ?: 0)
            )
        }
    }

    override fun validateInput(inputState: ExpensesAddScreenInputState): Boolean {
        return inputState.nameInput.isValid() &&
            inputState.amountInput.isValid() &&
            inputState.dateInput.isValid()
    }

    override fun shouldEnableSaveButton(inputState: ExpensesAddScreenInputState): Boolean {
        return validateInput(inputState)
    }

    override fun updateInput(
        update: (
            ExpensesAddScreenInputState
        ) -> ExpensesAddScreenInputState
    ) {
        inputState.update { currentState ->
            update(currentState)
        }
    }

}
