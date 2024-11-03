package com.eyther.lumbridge.data.mappers.recurringpayments

import com.eyther.lumbridge.data.model.recurringpayments.entity.RecurringPaymentEntity
import com.eyther.lumbridge.data.model.recurringpayments.local.RecurringPaymentCached
import com.eyther.lumbridge.shared.time.model.Periodicity

fun RecurringPaymentCached.toEntity(): RecurringPaymentEntity {
    return RecurringPaymentEntity(
        startDate = startDate,
        lastPaymentDate = lastPaymentDate,
        label = label,
        shouldNotifyWhenPaid = shouldNotifyWhenPaid,
        categoryTypeOrdinal = categoryTypeOrdinal,
        amountToPay = amountToPay,
        periodicityTypeOrdinal = periodicity.ordinal,
        numOfDays = (periodicity as? Periodicity.EveryXDays)?.numOfDays,
        numOfWeeks = (periodicity as? Periodicity.EveryXWeeks)?.numOfWeeks,
        numOfMonths = (periodicity as? Periodicity.EveryXMonths)?.numOfMonth,
        numOfYears = (periodicity as? Periodicity.EveryXYears)?.numOfYear,
        dayOfWeekOrdinal = (periodicity as? Periodicity.EveryXWeeks)?.dayOfWeekOrdinal,
        dayOfMonth = (periodicity as? Periodicity.EveryXMonths)?.dayOfMonth,
        monthOfYearOrdinal = (periodicity as? Periodicity.EveryXYears)?.monthOrdinal
    )
}
