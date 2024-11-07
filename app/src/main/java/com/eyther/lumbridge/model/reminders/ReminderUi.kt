package com.eyther.lumbridge.model.reminders

import com.eyther.lumbridge.model.time.RemindMeInUi
import java.time.LocalDateTime

data class ReminderUi(
    val reminderId: Long = -1,
    val label: String,
    val dueDate: LocalDateTime,
    val remindMeIn: RemindMeInUi,
    val alreadyNotified: Boolean
) {
    fun noLongerRelevant() = isInThePast() || alreadyNotified
    fun isInThePast() = dueDate.isBefore(LocalDateTime.now())
}
