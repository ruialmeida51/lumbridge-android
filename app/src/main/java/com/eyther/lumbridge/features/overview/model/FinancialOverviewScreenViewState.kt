package com.eyther.lumbridge.features.overview.model

import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.model.finance.NetSalaryUi

sealed interface FinancialOverviewScreenViewState {
    data object Loading : FinancialOverviewScreenViewState

    sealed class Content(
        open val locale: SupportedLocales
    ) : FinancialOverviewScreenViewState {

        // Input mode - we need to ask the user to input his economic profile
        data class Input(
            override val locale: SupportedLocales
        ) : Content(locale)

        // This state should only be reached if we actually have data to display.
        data class Overview(
            override val locale: SupportedLocales,
            val annualGrossSalary: Float,
            val netSalary: NetSalaryUi
        ) : Content(locale)
    }
}
