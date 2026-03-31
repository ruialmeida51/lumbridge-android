package com.eyther.lumbridge.domain.repository.reminders

import com.eyther.lumbridge.domain.mapper.reminders.toCached
import com.eyther.lumbridge.domain.mapper.reminders.toDomain
import com.eyther.lumbridge.domain.model.reminders.ReminderDomain
import com.eyther.lumbridge.shared.di.model.Schedulers
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface RemindersRepository {
    val remindersFlow
    suspend fun getAllReminders(): List<ReminderDomain>
    suspend fun saveReminder(reminderDomain: ReminderDomain)
    suspend fun deleteReminderById(reminderId: Long)
    suspend fun getReminderById(reminderId: Long): ReminderDomain?
}
