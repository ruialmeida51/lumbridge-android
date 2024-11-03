package com.eyther.lumbridge.data.mappers.recurringpayments

import com.eyther.lumbridge.data.model.recurringpayments.entity.RecurringPaymentEntity
import com.eyther.lumbridge.data.model.recurringpayments.local.RecurringPaymentCached
import com.eyther.lumbridge.shared.time.model.Periodicity

fun RecurringPaymentEntity.toCached(): RecurringPaymentCached {
    return RecurringPaymentCached(
        id = recurringPaymentId,
        startDate = startDate,
        lastPaymentDate = lastPaymentDate,
        periodicity = createPeriodicity(),
        label = label,
        amountToPay = amountToPay,
        categoryTypeOrdinal = categoryTypeOrdinal,
        shouldNotifyWhenPaid = shouldNotifyWhenPaid
    )
}

fun RecurringPaymentEntity.createPeriodicity(): Periodicity {
    return when (periodicityTypeOrdinal) {
        Periodicity.EVERY_X_DAYS -> Periodicity.EveryXDays(
            numOfDays = checkNotNull(numOfDays)
        )

        Periodicity.EVERY_X_WEEKS -> Periodicity.EveryXWeeks(
            numOfWeeks = checkNotNull(numOfWeeks),
            dayOfWeekOrdinal = checkNotNull(dayOfWeekOrdinal)
        )

        Periodicity.EVERY_X_MONTHS -> Periodicity.EveryXMonths(
            numOfMonth = checkNotNull(numOfMonths),
            dayOfMonth = checkNotNull(dayOfMonth)
        )

        Periodicity.EVERY_X_YEARS -> Periodicity.EveryXYears(
            numOfYear = checkNotNull(numOfYears),
            monthOrdinal = checkNotNull(monthOfYearOrdinal)
        )

        else -> {
            throw IllegalArgumentException("Invalid periodicity type ordinal: $periodicityTypeOrdinal")
        }
    }
}
