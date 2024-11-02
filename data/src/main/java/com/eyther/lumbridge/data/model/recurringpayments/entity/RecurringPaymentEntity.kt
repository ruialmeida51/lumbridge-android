package com.eyther.lumbridge.data.model.recurringpayments.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eyther.lumbridge.shared.time.model.Periodicity

const val RECURRING_PAYMENTS_TABLE_NAME = "recurring_payments"

@Entity(
    tableName = RECURRING_PAYMENTS_TABLE_NAME
)
data class RecurringPaymentEntity(
    @PrimaryKey(autoGenerate = true) val recurringPaymentId: Long = 0,
    val startDate: String,
    val lastPaymentDate: String?,
    val periodicity: Periodicity,
    val label: String,
    val amountToPay: Float,
    val categoryTypeOrdinal: Int,
    val shouldNotifyWhenPaid: Boolean
)
