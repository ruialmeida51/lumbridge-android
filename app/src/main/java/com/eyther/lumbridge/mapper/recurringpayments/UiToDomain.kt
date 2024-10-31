package com.eyther.lumbridge.mapper.recurringpayments

import com.eyther.lumbridge.domain.model.recurringpayments.model.RecurringPaymentDomain
import com.eyther.lumbridge.mapper.expenses.toDomain
import com.eyther.lumbridge.model.recurringpayments.RecurringPaymentUi

fun RecurringPaymentUi.toDomain() = RecurringPaymentDomain(
    id = id,
    startDate = startDate,
    lastPaymentDate = lastPaymentDate,
    periodicity = periodicity,
    label = label,
    shouldNotifyWhenPaid = shouldNotifyWhenPaid,
    amountToPay = amountToPay,
    categoryTypes = categoryTypesUi.toDomain()
)
