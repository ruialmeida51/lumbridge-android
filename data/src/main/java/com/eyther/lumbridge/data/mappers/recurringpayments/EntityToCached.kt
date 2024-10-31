package com.eyther.lumbridge.data.mappers.recurringpayments

import com.eyther.lumbridge.data.model.recurringpayments.entity.RecurringPaymentEntity
import com.eyther.lumbridge.data.model.recurringpayments.local.RecurringPaymentCached

fun RecurringPaymentEntity.toCached(): RecurringPaymentCached {
    return RecurringPaymentCached(
        id = recurringPaymentId,
        startDate = startDate,
        lastPaymentDate = lastPaymentDate,
        periodicity = periodicity,
        label = label,
        amountToPay = amountToPay,
        categoryTypeOrdinal = categoryTypeOrdinal,
        shouldNotifyWhenPaid = shouldNotifyWhenPaid
    )
}
