package com.eyther.lumbridge.domain.mapper.reminders

import com.eyther.lumbridge.data.model.reminders.local.ReminderCached
import com.eyther.lumbridge.domain.model.reminders.ReminderDomain
import com.eyther.lumbridge.shared.time.extensions.toLocalDateTime

fun ReminderCached.toDomain() = ReminderDomain(
    reminderId = reminderId,
    label = label,
    dueDate = dueDate.toLocalDateTime(),
    remindMeIn = remindMeIn,
    alreadyNotified = alreadyNotified
)

fun List<ReminderCached>.toDomain() = map { it.toDomain() }
