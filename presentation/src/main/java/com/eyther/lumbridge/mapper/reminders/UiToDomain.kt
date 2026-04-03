package com.eyther.lumbridge.mapper.reminders

import com.eyther.lumbridge.domain.model.reminders.ReminderDomain
import com.eyther.lumbridge.model.reminders.ReminderUi
import com.eyther.lumbridge.model.time.RemindMeInUi
import com.eyther.lumbridge.shared.time.model.RemindMeIn

fun ReminderUi.toDomain() = ReminderDomain(
    reminderId = reminderId,
    label = label,
    dueDate = dueDate,
    remindMeIn = remindMeIn.toDomain(),
    alreadyNotified = alreadyNotified
)

fun RemindMeInUi.toDomain(): RemindMeIn {
    return when (this) {
        is RemindMeInUi.FifteenMinutes -> RemindMeIn.FifteenMinutes
        is RemindMeInUi.ThirtyMinutes -> RemindMeIn.ThirtyMinutes
        is RemindMeInUi.OneHour -> RemindMeIn.OneHour
        is RemindMeInUi.TwoHours -> RemindMeIn.TwoHours
        is RemindMeInUi.OneDay -> RemindMeIn.OneDay
        is RemindMeInUi.TwoDays -> RemindMeIn.TwoDays
        is RemindMeInUi.XDaysBefore -> RemindMeIn.XDaysBefore(days)
        is RemindMeInUi.XHoursBefore -> RemindMeIn.XHoursBefore(hours)
        is RemindMeInUi.XMinutesBefore -> RemindMeIn.XMinutesBefore(minutes)
    }
}
