package com.eyther.lumbridge.domain.time

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.Month
import java.time.Period
import java.time.Year
import java.time.ZoneId
import java.time.format.DateTimeFormatter

const val MONTHS_IN_YEAR = 12
const val DAYS_IN_MONTH = 30 // Average number of days in a month

/**
 * Converts a [LocalDate] to a [String] in the format [DateTimeFormatter.ISO_LOCAL_DATE].
 *
 * @return [String] representation of the [LocalDate].
 */
fun LocalDate.toIsoLocalDateString(): String =
    format(DateTimeFormatter.ISO_LOCAL_DATE)

/**
 * Calculates the number of months between two [LocalDate]s.
 *
 * @param other [LocalDate] to calculate the difference to.
 * @param startOnDayOne If true, the difference will be calculated from the first day of the month.
 *
 * @return Number of months between the two [LocalDate]s.
 */
fun LocalDate.monthsUntil(other: LocalDate, startOnDayOne: Boolean = true): Int {
    val periodBetween = if (startOnDayOne) {
        Period.between(this.withDayOfMonth(1), other.withDayOfMonth(1))
    } else {
        Period.between(this, other)
    }

    return (periodBetween.years * MONTHS_IN_YEAR) +
        periodBetween.months +
        (periodBetween.days / DAYS_IN_MONTH)
}

/**
 * Converts a [String] to a [LocalDate] in the format [DateTimeFormatter.ISO_LOCAL_DATE].
 *
 * @return [LocalDate] representation of the [String].
 */
fun String.toLocalDate(): LocalDate =
    LocalDate.parse(this, DateTimeFormatter.ISO_LOCAL_DATE)

/**
 * Converts a [Long] to a [LocalDate] in the format [DateTimeFormatter.ISO_LOCAL_DATE].
 *
 * @return [LocalDate] representation of the [Long].
 * @see LocalDate.ofEpochDay
 */
fun Long.toLocalDate(): LocalDate = Instant.ofEpochMilli(this)
    .atZone(ZoneId.systemDefault())
    .toLocalDate()

/**
 * Converts a [Pair] of [Year] and [Month] to a [LocalDate].
 *
 * @return [LocalDate] representation of the [Pair].
 */
fun Pair<Year, Month>.toLocalDate(): LocalDate = LocalDate.of(first.value, second, DayOfWeek.MONDAY.value)
