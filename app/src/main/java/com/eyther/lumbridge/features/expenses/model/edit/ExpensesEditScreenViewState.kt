package com.eyther.lumbridge.features.expenses.model.edit

import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryTypes

sealed interface ExpensesEditScreenViewState {
    data object Loading : ExpensesEditScreenViewState

    data class Content(
        val inputState: ExpensesEditScreenInputState,
        val availableCategories: List<ExpensesCategoryTypes>,
        val shouldEnableSaveButton: Boolean
    ) : ExpensesEditScreenViewState

    fun asContent(): Content? = this as? Content
}
