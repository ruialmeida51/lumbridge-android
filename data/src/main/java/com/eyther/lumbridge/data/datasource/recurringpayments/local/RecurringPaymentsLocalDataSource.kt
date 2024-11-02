package com.eyther.lumbridge.data.datasource.recurringpayments.local

import android.util.Log
import com.eyther.lumbridge.data.datasource.recurringpayments.dao.RecurringPaymentsDao
import com.eyther.lumbridge.data.mappers.recurringpayments.toCached
import com.eyther.lumbridge.data.mappers.recurringpayments.toEntity
import com.eyther.lumbridge.data.model.recurringpayments.local.RecurringPaymentCached
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecurringPaymentsLocalDataSource @Inject constructor(
    private val recurringPaymentsDao: RecurringPaymentsDao
) {
    val recurringPaymentsFlow = recurringPaymentsDao
        .getRecurringPaymentsFlow()
        .map { flowItem ->
            flowItem?.map { recurringPaymentEntity -> recurringPaymentEntity.toCached() }
        }

    suspend fun getAllRecurringPayments(): List<RecurringPaymentCached>? {
        return recurringPaymentsDao.getAllRecurringPayments()
            ?.map { recurringPaymentEntity -> recurringPaymentEntity.toCached() }
    }

    suspend fun saveRecurringPayment(recurringPaymentCached: RecurringPaymentCached) {
        if (recurringPaymentCached.id == -1L) {
            recurringPaymentsDao.insertRecurringPayment(recurringPaymentCached.toEntity())
        } else {
            recurringPaymentsDao.updateRecurringPayment(recurringPaymentCached.toEntity()
                .copy(recurringPaymentId = recurringPaymentCached.id))
        }
    }

    suspend fun deleteRecurringPaymentById(recurringPaymentId: Long) {
        recurringPaymentsDao.deleteRecurringPaymentById(recurringPaymentId)
    }

    suspend fun getRecurringPaymentById(recurringPaymentId: Long): RecurringPaymentCached? {
        return recurringPaymentsDao.getRecurringPaymentById(recurringPaymentId)?.toCached()
    }
}
