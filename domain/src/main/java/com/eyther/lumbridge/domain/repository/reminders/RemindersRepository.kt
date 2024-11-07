package com.eyther.lumbridge.domain.repository.reminders

import com.eyther.lumbridge.data.datasource.reminders.local.RemindersLocalDataSource
import com.eyther.lumbridge.domain.mapper.reminders.toCached
import com.eyther.lumbridge.domain.mapper.reminders.toDomain
import com.eyther.lumbridge.domain.model.reminders.ReminderDomain
import com.eyther.lumbridge.shared.di.model.Schedulers
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemindersRepository @Inject constructor(
    private val remindersLocalDataSource: RemindersLocalDataSource,
    private val schedulers: Schedulers
) {
    val remindersFlow = remindersLocalDataSource
        .remindersFlow
        .mapNotNull { reminder ->
            reminder?.toDomain()
        }

    suspend fun getAllReminders(): List<ReminderDomain> = withContext(schedulers.io) {
        remindersLocalDataSource.getAllReminders()
            .orEmpty()
            .map { it.toDomain() }
    }

    suspend fun saveReminder(reminderDomain: ReminderDomain) = withContext(schedulers.io) {
        remindersLocalDataSource.saveReminder(reminderDomain.toCached())
    }

    suspend fun deleteReminderById(reminderId: Long) = withContext(schedulers.io) {
        remindersLocalDataSource.deleteReminderById(reminderId)
    }

    suspend fun getReminderById(reminderId: Long): ReminderDomain? = withContext(schedulers.io) {
        remindersLocalDataSource.getReminderById(reminderId)?.toDomain()
    }
}
