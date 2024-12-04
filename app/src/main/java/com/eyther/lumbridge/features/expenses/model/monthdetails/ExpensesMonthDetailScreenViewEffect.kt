package com.eyther.lumbridge.features.expenses.model.monthdetails

sealed interface ExpensesMonthDetailScreenViewEffect {
    data object NavigateBack : ExpensesMonthDetailScreenViewEffect

    data class ShowError(
        val message: String
    ) : ExpensesMonthDetailScreenViewEffect
}