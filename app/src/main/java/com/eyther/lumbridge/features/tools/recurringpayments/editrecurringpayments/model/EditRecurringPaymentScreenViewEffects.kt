package com.eyther.lumbridge.features.tools.recurringpayments.editrecurringpayments.model

sealed interface EditRecurringPaymentScreenViewEffects {
    data object CloseScreen : EditRecurringPaymentScreenViewEffects
}
