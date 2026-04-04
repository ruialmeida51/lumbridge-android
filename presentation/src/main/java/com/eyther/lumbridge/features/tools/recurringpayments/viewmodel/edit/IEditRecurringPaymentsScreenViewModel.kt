package com.eyther.lumbridge.features.tools.recurringpayments.viewmodel.edit

import com.eyther.lumbridge.features.tools.recurringpayments.model.edit.EditRecurringPaymentScreenViewEffects
import com.eyther.lumbridge.features.tools.recurringpayments.model.edit.EditRecurringPaymentsScreenViewState
import com.eyther.lumbridge.features.tools.recurringpayments.viewmodel.edit.delegate.IEditRecurringPaymentInputHandler
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IEditRecurringPaymentsScreenViewModel : IEditRecurringPaymentInputHandler {
    val viewState: StateFlow<EditRecurringPaymentsScreenViewState>
    val viewEffects: SharedFlow<EditRecurringPaymentScreenViewEffects>

    /**
     * Attempts to delete a recurring payment with the given ID.
     *
     * @param recurringPaymentId The ID of the recurring payment to delete.
     */
    fun onDeleteRecurringPayment(recurringPaymentId: Long)

    /**
     * Saves the recurring payment.
     */
    fun saveRecurringPayment()

    /**
     * Gets how much years the in future the date picker should allow.
     */
    fun getMaxSelectableYear(): Int
}
