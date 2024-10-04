package com.eyther.lumbridge.features.expenses.model.overview

import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewFilter.Companion.DisplayFilter
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewSortBy.Companion.DisplaySortBy
import com.eyther.lumbridge.model.expenses.ExpensesMonthUi
import com.eyther.lumbridge.model.finance.NetSalaryUi

sealed interface ExpensesOverviewScreenViewState {
    fun getDefaultDisplaySortBy(): ExpensesOverviewSortBy = ExpensesOverviewSortBy.DateDescending
    fun getDefaultDisplayFilter(): ExpensesOverviewFilter = ExpensesOverviewFilter.None

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
        open val expensesMonthUi: List<ExpensesMonthUi>,
        open val availableSorts: List<DisplaySortBy>,
        open val availableFilters: List<DisplayFilter>,
        open val selectedSort: ExpensesOverviewSortBy,
        open val selectedFilter: ExpensesOverviewFilter
    ) : ExpensesOverviewScreenViewState {
        data class HasFinancialProfile(
            val netSalaryUi: NetSalaryUi,
            override val locale: SupportedLocales,
            override val totalExpenses: Float,
            override val expensesMonthUi: List<ExpensesMonthUi>,
            override val availableSorts: List<DisplaySortBy>,
            override val availableFilters: List<DisplayFilter>,
            override val selectedSort: ExpensesOverviewSortBy,
            override val selectedFilter: ExpensesOverviewFilter
        ) : Content(
            locale = locale,
            totalExpenses = totalExpenses,
            expensesMonthUi = expensesMonthUi,
            availableSorts = availableSorts,
            availableFilters = availableFilters,
            selectedSort = selectedSort,
            selectedFilter = selectedFilter
        )

        data class NoFinancialProfile(
            override val locale: SupportedLocales,
            override val totalExpenses: Float,
            override val expensesMonthUi: List<ExpensesMonthUi>,
            override val availableSorts: List<DisplaySortBy>,
            override val availableFilters: List<DisplayFilter>,
            override val selectedSort: ExpensesOverviewSortBy,
            override val selectedFilter: ExpensesOverviewFilter
        ) : Content(
            locale = locale,
            totalExpenses = totalExpenses,
            expensesMonthUi = expensesMonthUi,
            availableSorts = availableSorts,
            availableFilters = availableFilters,
            selectedSort = selectedSort,
            selectedFilter = selectedFilter
        )
    }

    fun asContent(): Content = this as Content
    fun isContent(): Boolean = this is Content

    fun hasExpensesOrFilterApplied() = this is Content && (expensesMonthUi.isNotEmpty() || selectedFilter != getDefaultDisplayFilter())
    fun hasExpenses() = this is Content && expensesMonthUi.isNotEmpty()
}
