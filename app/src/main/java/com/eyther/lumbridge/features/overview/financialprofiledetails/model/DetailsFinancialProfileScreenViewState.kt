package com.eyther.lumbridge.features.overview.financialprofiledetails.model

import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.model.finance.NetSalaryUi

sealed interface DetailsFinancialProfileScreenViewState {
    data object Loading : DetailsFinancialProfileScreenViewState
    data object Empty : DetailsFinancialProfileScreenViewState

    data class Content(
        val salaryDetails: NetSalaryUi,
        val locale: SupportedLocales
    ) : DetailsFinancialProfileScreenViewState
}
