package com.eyther.lumbridge.domain.model.reminders

import com.eyther.lumbridge.shared.time.model.RemindMeIn
import java.time.LocalDateTime

/**
 * Represents a reminder that should be shown to the user at a given time.
 *
 * @param reminderId The unique identifier of the reminder.
 * @param label A user given label for the reminder.
 * @param dueDate The date the task for the reminder is due.
 * @param remindMeIn The time period to remind the user in.
 */
data class ReminderDomain(
    val reminderId: Long = -1,
    val label: String,
    val dueDate: LocalDateTime,
    val remindMeIn: RemindMeIn,
    val alreadyNotified: Boolean
)
