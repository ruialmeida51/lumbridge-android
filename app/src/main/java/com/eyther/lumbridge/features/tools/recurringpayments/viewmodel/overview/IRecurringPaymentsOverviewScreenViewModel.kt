package com.eyther.lumbridge.features.tools.recurringpayments.viewmodel.overview

import com.eyther.lumbridge.features.tools.recurringpayments.model.overview.RecurringPaymentsOverviewScreenViewEffects
import com.eyther.lumbridge.features.tools.recurringpayments.model.overview.RecurringPaymentsOverviewScreenViewState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IRecurringPaymentsOverviewScreenViewModel {
    val viewState: StateFlow<RecurringPaymentsOverviewScreenViewState>
    val viewEffects: SharedFlow<RecurringPaymentsOverviewScreenViewEffects>

    fun deleteRecurringPayment(recurringPaymentId: Long)
}
