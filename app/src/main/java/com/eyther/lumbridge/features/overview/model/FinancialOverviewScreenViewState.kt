package com.eyther.lumbridge.features.overview.model

import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.model.finance.MortgageUi
import com.eyther.lumbridge.model.finance.NetSalaryUi

sealed interface FinancialOverviewScreenViewState {
    data object Loading : FinancialOverviewScreenViewState

    data class Content(
        val locale: SupportedLocales,
        val selectedTabItem: FinancialOverviewTabItem = FinancialOverviewTabItem.PersonalOverview,
        val netSalary: NetSalaryUi? = null,
        val mortgage: MortgageUi? = null
    ) : FinancialOverviewScreenViewState

    fun asContent(): Content = this as Content
    fun isContent(): Boolean = this is Content
}
