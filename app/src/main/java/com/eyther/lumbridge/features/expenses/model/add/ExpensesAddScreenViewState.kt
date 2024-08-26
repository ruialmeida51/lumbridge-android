package com.eyther.lumbridge.features.expenses.model.add

import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryTypes

sealed interface ExpensesAddScreenViewState {
    data object Loading : ExpensesAddScreenViewState

    data class Content(
        val inputState: ExpensesAddScreenInputState,
        val availableCategories: List<ExpensesCategoryTypes>,
        val shouldEnableSaveButton: Boolean
    ) : ExpensesAddScreenViewState

    fun asContent(): Content = this as Content
}
