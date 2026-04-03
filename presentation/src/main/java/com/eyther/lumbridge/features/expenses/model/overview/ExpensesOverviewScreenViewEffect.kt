package com.eyther.lumbridge.features.expenses.model.overview

sealed interface ExpensesOverviewScreenViewEffect {
    data class ShowError(
        val message: String
    ) : ExpensesOverviewScreenViewEffect

    data class NavigateToMonthDetail(
        val year: Int,
        val month: Int
    ) : ExpensesOverviewScreenViewEffect
}
