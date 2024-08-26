package com.eyther.lumbridge.features.expenses.model.edit

sealed interface ExpensesEditScreenViewState {
    data object Loading : ExpensesEditScreenViewState

    data class Content(
        val inputState: ExpensesEditScreenInputState,
        val shouldEnableSaveButton: Boolean
    ) : ExpensesEditScreenViewState
}
