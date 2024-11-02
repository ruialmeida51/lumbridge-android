package com.eyther.lumbridge.mapper.recurringpayments

import com.eyther.lumbridge.domain.model.recurringpayments.RecurringPaymentDomain
import com.eyther.lumbridge.mapper.expenses.toUi
import com.eyther.lumbridge.model.recurringpayments.PeriodicityUi
import com.eyther.lumbridge.model.recurringpayments.RecurringPaymentUi
import com.eyther.lumbridge.shared.time.model.Periodicity
import java.time.LocalDate

fun RecurringPaymentDomain.toUi() = RecurringPaymentUi(
    id = id,
    startDate = startDate,
    mostRecentPaymentDate = lastPaymentDate,
    periodicity = periodicity.toUi(lastPaymentDate ?: startDate),
    label = label,
    shouldNotifyWhenPaid = shouldNotifyWhenPaid,
    amountToPay = amountToPay,
    categoryTypesUi = categoryTypes.toUi()
)

fun Periodicity.toUi(
    startFrom: LocalDate
): PeriodicityUi {
    return when(this) {
        is Periodicity.EveryXDays -> PeriodicityUi.EveryXDays(
            nextDueDate = getNextDate(startFrom),
            numOfDays = numOfDays
        )
        is Periodicity.EveryXWeeks -> PeriodicityUi.EveryXWeeks(
            nextDueDate = getNextDate(startFrom),
            numOfWeeks = numOfWeeks,
            dayOfWeek = dayOfWeek
        )
        is Periodicity.EveryXMonths -> PeriodicityUi.EveryXMonths(
            nextDueDate = getNextDate(startFrom),
            numOfMonth = numOfMonth,
            dayOfMonth = dayOfMonth
        )
        is Periodicity.EveryXYears -> PeriodicityUi.EveryXYears(
            nextDueDate = getNextDate(startFrom),
            numOfYear = numOfYear,
            month = month
        )
    }
}

fun List<RecurringPaymentDomain>.toUi() = map { it.toUi() }
