package com.eyther.lumbridge.features.tools.recurringpayments.overview.model

import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.model.recurringpayments.RecurringPaymentUi

sealed interface RecurringPaymentsOverviewScreenViewState {
    data object Loading : RecurringPaymentsOverviewScreenViewState
    data object Empty : RecurringPaymentsOverviewScreenViewState

    data class Content(
        val recurringPayments: List<RecurringPaymentUi>,
        val locale: SupportedLocales
    ) : RecurringPaymentsOverviewScreenViewState
}
