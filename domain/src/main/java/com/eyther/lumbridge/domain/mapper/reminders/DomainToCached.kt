package com.eyther.lumbridge.domain.mapper.reminders

import com.eyther.lumbridge.data.model.reminders.local.ReminderCached
import com.eyther.lumbridge.domain.model.reminders.ReminderDomain
import com.eyther.lumbridge.shared.time.extensions.toIsoLocalDateTimeString

fun ReminderDomain.toCached() = ReminderCached(
    reminderId = reminderId,
    label = label,
    dueDate = dueDate.toIsoLocalDateTimeString(),
    remindMeIn = remindMeIn,
    alreadyNotified = alreadyNotified
)
