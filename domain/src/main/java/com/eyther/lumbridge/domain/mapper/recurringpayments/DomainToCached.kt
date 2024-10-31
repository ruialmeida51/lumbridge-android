package com.eyther.lumbridge.domain.mapper.recurringpayments

import com.eyther.lumbridge.data.model.recurringpayments.local.RecurringPaymentCached
import com.eyther.lumbridge.domain.model.recurringpayments.model.RecurringPaymentDomain
import com.eyther.lumbridge.shared.time.extensions.toIsoLocalDateString

fun RecurringPaymentDomain.toCached() = RecurringPaymentCached(
    id = id,
    label = label,
    startDate = startDate.toIsoLocalDateString(),
    lastPaymentDate = lastPaymentDate?.toIsoLocalDateString(),
    periodicity = periodicity,
    shouldNotifyWhenPaid = shouldNotifyWhenPaid,
    amountToPay = amountToPay,
    categoryTypeOrdinal = categoryTypes.ordinal
)
