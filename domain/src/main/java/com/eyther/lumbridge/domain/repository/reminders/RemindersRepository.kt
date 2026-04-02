package com.eyther.lumbridge.domain.repository.reminders

import com.eyther.lumbridge.domain.model.reminders.ReminderDomain
import kotlinx.coroutines.flow.Flow

interface RemindersRepository {
    val remindersFlow: Flow<List<ReminderDomain>>
    suspend fun getAllReminders(): List<ReminderDomain>
    suspend fun saveReminder(reminderDomain: ReminderDomain)
    suspend fun deleteReminderById(reminderId: Long)
    suspend fun getReminderById(reminderId: Long): ReminderDomain?
}
