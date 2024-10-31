package com.eyther.lumbridge.features.expenses.viewmodel.edit.delegate

import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.getErrorOrNull
import com.eyther.lumbridge.features.expenses.model.add.ExpensesAddSurplusOrExpenseChoice.Surplus
import com.eyther.lumbridge.features.expenses.model.edit.ExpensesEditScreenInputState
import com.eyther.lumbridge.model.expenses.ExpensesCategoryTypesUi
import com.eyther.lumbridge.shared.time.extensions.toLocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ExpensesEditScreenInputHandler @Inject constructor() : IExpensesEditScreenInputHandler {
    override val inputState = MutableStateFlow(ExpensesEditScreenInputState())

    private var cachedCategorySelection: ExpensesCategoryTypesUi? = null

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

    override fun onTypeChanged(typeOrdinal: Int?) {
        updateInput { state ->
            state.copy(
                categoryType = ExpensesCategoryTypesUi.of(typeOrdinal ?: 0)
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

    override fun onSurplusOrExpenseChanged(choiceOrdinal: Int) {
        updateInput { state ->
            state.copy(
                surplusOrExpenseChoice = state.surplusOrExpenseChoice.copy(
                    selectedTab = choiceOrdinal
                ),
                categoryType = if (isSurplus(choiceOrdinal)) {
                    // Cache the category selection if it's a surplus, as we need to restore it when switching back to expenses.
                    cachedCategorySelection = state.categoryType
                    // Force select the surplus category when switching to surplus.
                    ExpensesCategoryTypesUi.Surplus
                } else {
                    // Restore the cached category selection when switching back to expenses.
                    cachedCategorySelection ?: ExpensesCategoryTypesUi.Food
                }
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

    private fun isSurplus(selectedOrdinal: Int): Boolean {
        return selectedOrdinal == Surplus.ordinal
    }
}
