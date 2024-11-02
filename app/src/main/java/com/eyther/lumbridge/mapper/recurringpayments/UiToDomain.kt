package com.eyther.lumbridge.mapper.recurringpayments

import com.eyther.lumbridge.domain.model.recurringpayments.RecurringPaymentDomain
import com.eyther.lumbridge.mapper.expenses.toDomain
import com.eyther.lumbridge.model.recurringpayments.PeriodicityUi
import com.eyther.lumbridge.model.recurringpayments.RecurringPaymentUi
import com.eyther.lumbridge.shared.time.model.Periodicity

fun RecurringPaymentUi.toDomain() = RecurringPaymentDomain(
    id = id,
    startDate = startDate,
    lastPaymentDate = mostRecentPaymentDate,
    periodicity = periodicity.toDomain(),
    label = label,
    shouldNotifyWhenPaid = shouldNotifyWhenPaid,
    amountToPay = amountToPay,
    categoryTypes = categoryTypesUi.toDomain()
)

fun PeriodicityUi.toDomain(): Periodicity {
    return when(this) {
        is PeriodicityUi.EveryXDays -> Periodicity.EveryXDays(
            numOfDays = numOfDays
        )
        is PeriodicityUi.EveryXWeeks -> Periodicity.EveryXWeeks(
            numOfWeeks = numOfWeeks,
            dayOfWeekOrdinal = dayOfWeek.ordinal + 1 // DayOfWeek is 1-indexed
        )
        is PeriodicityUi.EveryXMonths -> Periodicity.EveryXMonths(
            numOfMonth = numOfMonth,
            dayOfMonth = dayOfMonth
        )
        is PeriodicityUi.EveryXYears -> Periodicity.EveryXYears(
            numOfYear = numOfYear,
            monthOrdinal = month.ordinal + 1 // Month is 1-indexed
        )
    }
}
