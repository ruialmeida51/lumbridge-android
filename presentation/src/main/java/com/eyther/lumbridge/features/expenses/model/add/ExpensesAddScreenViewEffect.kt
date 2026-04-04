package com.eyther.lumbridge.features.expenses.model.add

sealed interface ExpensesAddScreenViewEffect {
    data object Finish : ExpensesAddScreenViewEffect
    data class ShowError(val message: String) : ExpensesAddScreenViewEffect
}
