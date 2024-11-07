package com.eyther.lumbridge.mapper.reminders

import com.eyther.lumbridge.domain.model.reminders.ReminderDomain
import com.eyther.lumbridge.model.reminders.ReminderUi
import com.eyther.lumbridge.model.time.RemindMeInUi
import com.eyther.lumbridge.shared.time.model.RemindMeIn
import java.time.LocalDateTime

fun ReminderDomain.toUi() = ReminderUi(
    reminderId = reminderId,
    label = label,
    dueDate = dueDate,
    remindMeIn = remindMeIn.toUi(dueDate),
    alreadyNotified = alreadyNotified
)

fun List<ReminderDomain>.toUi() = map { it.toUi() }

fun RemindMeIn.toUi(dueDate: LocalDateTime): RemindMeInUi {
    return when (this) {
        is RemindMeIn.FifteenMinutes -> RemindMeInUi.FifteenMinutes(
            reminderTime = getReminderTime(dueDate)
        )

        is RemindMeIn.ThirtyMinutes -> RemindMeInUi.ThirtyMinutes(
            reminderTime = getReminderTime(dueDate)
        )

        is RemindMeIn.OneHour -> RemindMeInUi.OneHour(
            reminderTime = getReminderTime(dueDate)
        )

        is RemindMeIn.TwoHours -> RemindMeInUi.TwoHours(
            reminderTime = getReminderTime(dueDate)
        )

        is RemindMeIn.OneDay -> RemindMeInUi.OneDay(
            reminderTime = getReminderTime(dueDate)
        )

        is RemindMeIn.TwoDays -> RemindMeInUi.TwoDays(
            reminderTime = getReminderTime(dueDate)
        )

        is RemindMeIn.XDaysBefore -> RemindMeInUi.XDaysBefore(
            reminderTime = getReminderTime(dueDate),
            days = days
        )

        is RemindMeIn.XHoursBefore -> RemindMeInUi.XHoursBefore(
            reminderTime = getReminderTime(dueDate),
            hours = hours
        )

        is RemindMeIn.XMinutesBefore -> RemindMeInUi.XMinutesBefore(
            reminderTime = getReminderTime(dueDate),
            minutes = minutes
        )
    }
}
