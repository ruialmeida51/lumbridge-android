package com.eyther.lumbridge.features.tools.recurringpayments.overview.viewmodel

import com.eyther.lumbridge.features.tools.recurringpayments.overview.model.RecurringPaymentsOverviewScreenViewEffects
import com.eyther.lumbridge.features.tools.recurringpayments.overview.model.RecurringPaymentsOverviewScreenViewState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IRecurringPaymentsOverviewScreenViewModel {
    val viewState: StateFlow<RecurringPaymentsOverviewScreenViewState>
    val viewEffects: SharedFlow<RecurringPaymentsOverviewScreenViewEffects>

    fun deleteRecurringPayment(recurringPaymentId: Long)
}
