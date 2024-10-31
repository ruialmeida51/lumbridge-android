package com.eyther.lumbridge.shared.time.extensions

import java.time.Instant
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
 * Converts a [String] to a [LocalDateTime] in the format [DateTimeFormatter.ISO_LOCAL_DATE_TIME].
 *
 * @return [LocalDateTime] representation of the [String].
 * @see LocalDateTime.parse
 */
fun String.parseToISOZonedLocalDateTime(): LocalDateTime = LocalDateTime
    .parse(this, DateTimeFormatter.ISO_ZONED_DATE_TIME)

/**
 * Converts a [String] to a [Long] in milliseconds.
 *
 * @return [Long] millisecond representation of the [String].
 * @see LocalDateTime.parse
 */
fun String.parseISOZonedLocalDatetimeToMillis(): Long = parseToISOZonedLocalDateTime()
    .atZone(ZoneId.systemDefault())
    .toInstant()
    .toEpochMilli()
