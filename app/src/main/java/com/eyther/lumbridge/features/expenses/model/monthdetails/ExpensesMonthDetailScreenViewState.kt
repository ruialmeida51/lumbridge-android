package com.eyther.lumbridge.features.expenses.model.monthdetails

import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.model.expenses.ExpensesMonthUi

sealed interface ExpensesMonthDetailScreenViewState {
    data object Loading : ExpensesMonthDetailScreenViewState
    data object Error : ExpensesMonthDetailScreenViewState

    data class Content(
        val monthExpenses: ExpensesMonthUi,
        val showAllocations: Boolean,
        val locale: SupportedLocales
    ) : ExpensesMonthDetailScreenViewState

    fun asContent(): Content = this as Content
}
