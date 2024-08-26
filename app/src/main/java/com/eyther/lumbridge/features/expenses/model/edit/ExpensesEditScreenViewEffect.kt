package com.eyther.lumbridge.features.expenses.model.edit

sealed interface ExpensesEditScreenViewEffect {
    data object Finish : ExpensesEditScreenViewEffect

    data class ShowError(
        val message: String
    ) : ExpensesEditScreenViewEffect
}
