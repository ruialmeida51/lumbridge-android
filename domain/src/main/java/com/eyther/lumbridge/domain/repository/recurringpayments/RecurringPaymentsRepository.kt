package com.eyther.lumbridge.domain.repository.recurringpayments

import com.eyther.lumbridge.domain.mapper.recurringpayments.toCached
import com.eyther.lumbridge.domain.mapper.recurringpayments.toDomain
import com.eyther.lumbridge.domain.model.recurringpayments.RecurringPaymentDomain
import com.eyther.lumbridge.shared.di.model.Schedulers
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface RecurringPaymentsRepository {
    val recurringPaymentsFlow
    suspend fun getAllRecurringPayments(): List<RecurringPaymentDomain>
    suspend fun saveRecurringPayment(recurringPaymentDomain: RecurringPaymentDomain)
    suspend fun deleteRecurringPaymentById(recurringPaymentId: Long)
    suspend fun getRecurringPaymentById(recurringPaymentId: Long): RecurringPaymentDomain?
}
