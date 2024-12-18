package com.eyther.lumbridge.shared.time.extensions

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.Month
import java.time.Period
import java.time.Year
import java.time.ZoneId
import java.time.format.DateTimeFormatter

const val MONTHS_IN_YEAR = 12

/**
 * Converts a [LocalDate] to a [String] in the format [DateTimeFormatter.ISO_LOCAL_DATE].
 *
 * @return [String] representation of the [LocalDate].
 */
fun LocalDate.toIsoLocalDateString(): String =
    format(DateTimeFormatter.ISO_LOCAL_DATE)

/**
 * Converts a [LocalDate] to a [String] in the format "MMMM yyyy".
 */
fun LocalDate.toMonthYearDateString(): String =
    format(DateTimeFormatter.ofPattern("MMMM yyyy"))

/**
 * Converts a [LocalDate] to a [String] in the format "dd MMMM yyyy".
 */
fun LocalDate.toDayMonthYearDateString(): String =
    format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))

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

    return (periodBetween.years * MONTHS_IN_YEAR) + periodBetween.months
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

/**
 * Simple helper function because [LocalDate] doesn't have a isBeforeOrEqual function.
 */
fun LocalDate.isBeforeOrEqual(other: LocalDate): Boolean = isBefore(other) || isEqual(other)

/**
 * Simple helper function because [LocalDate] doesn't have a isAfterOrEqual function.
 */
fun LocalDate.isAfterOrEqual(other: LocalDate): Boolean = isAfter(other) || isEqual(other)
