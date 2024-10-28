package com.eyther.lumbridge.features.expenses.model.edit

import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryTypes
import com.eyther.lumbridge.model.expenses.ExpensesCategoryTypesUi

sealed interface ExpensesEditScreenViewState {
    data object Loading : ExpensesEditScreenViewState

    data class Content(
        val inputState: ExpensesEditScreenInputState,
        val availableCategories: List<ExpensesCategoryTypesUi>,
        val shouldEnableSaveButton: Boolean
    ) : ExpensesEditScreenViewState
}
