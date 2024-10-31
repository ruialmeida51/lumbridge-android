package com.eyther.lumbridge.mapper.recurringpayments

import com.eyther.lumbridge.domain.model.recurringpayments.model.RecurringPaymentDomain
import com.eyther.lumbridge.mapper.expenses.toUi
import com.eyther.lumbridge.model.recurringpayments.RecurringPaymentUi

fun RecurringPaymentDomain.toUi() = RecurringPaymentUi(
    id = id,
    startDate = startDate,
    lastPaymentDate = lastPaymentDate,
    periodicity = periodicity,
    label = label,
    shouldNotifyWhenPaid = shouldNotifyWhenPaid,
    amountToPay = amountToPay,
    categoryTypesUi = categoryTypes.toUi()
)
