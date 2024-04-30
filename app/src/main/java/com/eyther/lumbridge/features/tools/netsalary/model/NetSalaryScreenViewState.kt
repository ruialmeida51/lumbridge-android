package com.eyther.lumbridge.features.tools.netsalary.model

import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.model.finance.NetSalaryUi

sealed interface NetSalaryScreenViewState {
    data object Loading : NetSalaryScreenViewState

    sealed class Content(
        open val annualGrossSalary: Float? = null,
        open val locale: SupportedLocales
    ) : NetSalaryScreenViewState {

        // Input mode - we still want to display data if we have it. Not a problem if we don't,
        // though.
        data class Input(
            override val annualGrossSalary: Float?,
            override val locale: SupportedLocales,
            val foodCardPerDiem: Float?
        ) : Content(annualGrossSalary, locale)

        // This state should only be reached if we actually have data to display.
        data class Overview(
            override val annualGrossSalary: Float,
            override val locale: SupportedLocales,
            val netSalary: NetSalaryUi
        ) : Content(annualGrossSalary, locale)
    }
}
