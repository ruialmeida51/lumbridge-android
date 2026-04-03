package com.eyther.lumbridge.domain.repository.recurringpayments

import com.eyther.lumbridge.domain.model.recurringpayments.RecurringPaymentDomain
import kotlinx.coroutines.flow.Flow

interface RecurringPaymentsRepository {
    val recurringPaymentsFlow: Flow<List<RecurringPaymentDomain>>
    suspend fun getAllRecurringPayments(): List<RecurringPaymentDomain>
    suspend fun saveRecurringPayment(recurringPaymentDomain: RecurringPaymentDomain)
    suspend fun deleteRecurringPaymentById(recurringPaymentId: Long)
    suspend fun getRecurringPaymentById(recurringPaymentId: Long): RecurringPaymentDomain?
}
