package com.eyther.lumbridge.shared.time.extensions

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Converts a [Long] to a [LocalDateTime] in the format [DateTimeFormatter.ISO_LOCAL_DATE].
 *
 * @return [LocalDateTime] representation of the [Long].
 * @see Instant.ofEpochMilli
 */
fun Long.toLocalDateTime(): LocalDateTime = Instant.ofEpochMilli(this)
    .atZone(ZoneId.systemDefault())
    .toLocalDateTime()

/**
 * Converts a [LocalDate] to a [String] in the format [DateTimeFormatter.ISO_LOCAL_DATE_TIME].
 *
 * @return [String] representation of the [LocalDate].
 */
fun LocalDate.toIsoLocalDateTimeString(): String =
    format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

/**
 * Converts a [LocalDate] to a [String] in the format "dd MMMM yyyy HH:mm".
 *
 * @return [String] representation of the [LocalDate].
 */
fun LocalDateTime.toDayMonthYearHourMinuteString(): String =
    format(DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm"))
