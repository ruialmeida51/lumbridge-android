package com.eyther.lumbridge.features.expenses.model.overview

import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.model.expenses.ExpensesMonthUi
import com.eyther.lumbridge.model.finance.NetSalaryUi

sealed interface ExpensesOverviewScreenViewState {
    data object Loading : ExpensesOverviewScreenViewState

    sealed interface Empty : ExpensesOverviewScreenViewState {
        data class HasFinancialProfile(
            val locale: SupportedLocales,
            val netSalaryUi: NetSalaryUi
        ) : Empty

        data object NoFinancialProfile : Empty
    }

    sealed class Content(
        open val locale: SupportedLocales,
        open val totalExpenses: Float,
        open val expensesMonthUi: List<ExpensesMonthUi>
    ) : ExpensesOverviewScreenViewState {

        data class HasFinancialProfile(
            val netSalaryUi: NetSalaryUi,
            override val locale: SupportedLocales,
            override val totalExpenses: Float,
            override val expensesMonthUi: List<ExpensesMonthUi>
        ) : Content(locale, totalExpenses, expensesMonthUi)

        data class NoFinancialProfile(
            override val locale: SupportedLocales,
            override val totalExpenses: Float,
            override val expensesMonthUi: List<ExpensesMonthUi>
        ) : Content(locale, totalExpenses, expensesMonthUi)
    }

    fun asContent(): Content = this as Content
    fun isContent(): Boolean = this is Content
}
