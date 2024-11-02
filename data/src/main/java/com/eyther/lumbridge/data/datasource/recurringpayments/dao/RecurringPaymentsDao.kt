package com.eyther.lumbridge.data.datasource.recurringpayments.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.eyther.lumbridge.data.model.recurringpayments.entity.RECURRING_PAYMENTS_TABLE_NAME
import com.eyther.lumbridge.data.model.recurringpayments.entity.RecurringPaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecurringPaymentsDao {
    @Transaction
    @Query("SELECT * FROM $RECURRING_PAYMENTS_TABLE_NAME")
    fun getRecurringPaymentsFlow(): Flow<List<RecurringPaymentEntity>?>

    @Transaction
    @Query("SELECT * FROM $RECURRING_PAYMENTS_TABLE_NAME")
    suspend fun getAllRecurringPayments(): List<RecurringPaymentEntity>?

    @Transaction
    @Query("SELECT * FROM $RECURRING_PAYMENTS_TABLE_NAME WHERE recurringPaymentId = :recurringPaymentId")
    suspend fun getRecurringPaymentById(recurringPaymentId: Long): RecurringPaymentEntity?

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecurringPayment(recurringPayment: RecurringPaymentEntity): Long

    @Transaction
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRecurringPayment(recurringPayment: RecurringPaymentEntity)

    @Transaction
    @Query("DELETE FROM $RECURRING_PAYMENTS_TABLE_NAME WHERE recurringPaymentId = :recurringPaymentId")
    suspend fun deleteRecurringPaymentById(recurringPaymentId: Long)
}
