package com.eyther.lumbridge.data.repository.recurringpayments

import com.eyther.lumbridge.data.datasource.recurringpayments.local.RecurringPaymentsLocalDataSource
import com.eyther.lumbridge.data.mapper.recurringpayments.toCached
import com.eyther.lumbridge.data.mapper.recurringpayments.toDomain
import com.eyther.lumbridge.domain.model.recurringpayments.RecurringPaymentDomain
import com.eyther.lumbridge.domain.repository.recurringpayments.RecurringPaymentsRepository
import com.eyther.lumbridge.shared.di.model.Schedulers
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecurringPaymentsRepositoryImpl @Inject constructor(
    private val recurringPaymentsLocalDataSource: RecurringPaymentsLocalDataSource,
    private val schedulers: Schedulers
) : RecurringPaymentsRepository {

    override val recurringPaymentsFlow = recurringPaymentsLocalDataSource
        .recurringPaymentsFlow
        .mapNotNull { recurringPayment ->
            recurringPayment?.toDomain()
        }

    override suspend fun getAllRecurringPayments(): List<RecurringPaymentDomain> = withContext(schedulers.io) {
        recurringPaymentsLocalDataSource.getAllRecurringPayments()
            .orEmpty()
            .map { it.toDomain() }
    }

    override suspend fun saveRecurringPayment(recurringPaymentDomain: RecurringPaymentDomain) = withContext(schedulers.io) {
        recurringPaymentsLocalDataSource.saveRecurringPayment(recurringPaymentDomain.toCached())
    }

    override suspend fun deleteRecurringPaymentById(recurringPaymentId: Long) = withContext(schedulers.io) {
        recurringPaymentsLocalDataSource.deleteRecurringPaymentById(recurringPaymentId)
    }

    override suspend fun getRecurringPaymentById(recurringPaymentId: Long): RecurringPaymentDomain? = withContext(schedulers.io) {
        recurringPaymentsLocalDataSource.getRecurringPaymentById(recurringPaymentId)?.toDomain()
    }
}
