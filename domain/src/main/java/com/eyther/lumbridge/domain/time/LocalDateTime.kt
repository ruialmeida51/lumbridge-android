package com.eyther.lumbridge.domain.time

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
