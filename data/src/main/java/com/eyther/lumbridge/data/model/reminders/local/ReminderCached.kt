package com.eyther.lumbridge.data.model.reminders.local

import com.eyther.lumbridge.shared.time.model.RemindMeIn

data class ReminderCached(
    val reminderId: Long = -1,
    val label: String,
    val dueDate: String,
    val remindMeIn: RemindMeIn,
    val alreadyNotified: Boolean
)
