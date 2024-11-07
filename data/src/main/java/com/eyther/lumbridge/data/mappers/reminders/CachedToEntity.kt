package com.eyther.lumbridge.data.mappers.reminders

import com.eyther.lumbridge.data.model.reminders.entity.ReminderEntity
import com.eyther.lumbridge.data.model.reminders.local.ReminderCached
import com.eyther.lumbridge.shared.time.model.RemindMeIn

fun ReminderCached.toEntity(): ReminderEntity {
    return ReminderEntity(
        label = label,
        dueDate = dueDate,
        remindMeInOrdinal = remindMeIn.ordinal,
        remindMeInDaysCustomValue = (remindMeIn as? RemindMeIn.XDaysBefore)?.days,
        remindMeInHoursCustomValue = (remindMeIn as? RemindMeIn.XHoursBefore)?.hours,
        remindMeInMinutesCustomValue = (remindMeIn as? RemindMeIn.XMinutesBefore)?.minutes,
        alreadyNotified = alreadyNotified
    )
}
