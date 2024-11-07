package com.eyther.lumbridge.data.mappers.reminders

import com.eyther.lumbridge.data.model.reminders.entity.ReminderEntity
import com.eyther.lumbridge.data.model.reminders.local.ReminderCached
import com.eyther.lumbridge.shared.time.model.RemindMeIn

fun ReminderEntity.toCached(): ReminderCached {
    return ReminderCached(
        reminderId = reminderId,
        label = label,
        dueDate = dueDate,
        alreadyNotified = alreadyNotified,
        remindMeIn = when (remindMeInOrdinal) {
            RemindMeIn.FifteenMinutes.ordinal -> RemindMeIn.FifteenMinutes
            RemindMeIn.ThirtyMinutes.ordinal -> RemindMeIn.ThirtyMinutes
            RemindMeIn.OneHour.ordinal -> RemindMeIn.OneHour
            RemindMeIn.TwoHours.ordinal -> RemindMeIn.TwoHours
            RemindMeIn.OneDay.ordinal -> RemindMeIn.OneDay
            RemindMeIn.TwoDays.ordinal -> RemindMeIn.TwoDays
            RemindMeIn.X_DAYS_BEFORE -> RemindMeIn.XDaysBefore(remindMeInDaysCustomValue!!)
            RemindMeIn.X_HOURS_BEFORE -> RemindMeIn.XHoursBefore(remindMeInHoursCustomValue!!)
            RemindMeIn.X_MINUTES_BEFORE -> RemindMeIn.XMinutesBefore(remindMeInMinutesCustomValue!!)
            else -> throw IllegalArgumentException("Unknown remind me in ordinal: $remindMeInOrdinal")
        }
    )
}
