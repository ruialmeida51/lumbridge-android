package com.eyther.lumbridge.data.mappers.recurringpayments

import com.eyther.lumbridge.data.model.recurringpayments.entity.RecurringPaymentEntity
import com.eyther.lumbridge.data.model.recurringpayments.local.RecurringPaymentCached

fun RecurringPaymentCached.toEntity(): RecurringPaymentEntity {
    return RecurringPaymentEntity(
        startDate = startDate,
        lastPaymentDate = lastPaymentDate,
        periodicity = periodicity,
        label = label,
        shouldNotifyWhenPaid = shouldNotifyWhenPaid,
        categoryTypeOrdinal = categoryTypeOrdinal,
        amountToPay = amountToPay
    )
}
