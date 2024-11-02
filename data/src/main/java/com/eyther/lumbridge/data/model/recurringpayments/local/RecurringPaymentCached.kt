package com.eyther.lumbridge.data.model.recurringpayments.local

import com.eyther.lumbridge.shared.time.model.Periodicity

data class RecurringPaymentCached(
    val id: Long = -1,
    val startDate: String,
    val lastPaymentDate: String?,
    val periodicity: Periodicity,
    val label: String,
    val amountToPay: Float,
    val categoryTypeOrdinal: Int,
    val shouldNotifyWhenPaid: Boolean
)
