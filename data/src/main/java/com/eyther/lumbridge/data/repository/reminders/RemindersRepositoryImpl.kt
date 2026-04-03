package com.eyther.lumbridge.data.repository.reminders

import com.eyther.lumbridge.data.datasource.reminders.local.RemindersLocalDataSource
import com.eyther.lumbridge.data.mapper.reminders.toCached
import com.eyther.lumbridge.data.mapper.reminders.toDomain
import com.eyther.lumbridge.domain.model.reminders.ReminderDomain
import com.eyther.lumbridge.domain.repository.reminders.RemindersRepository
import com.eyther.lumbridge.shared.di.model.Schedulers
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemindersRepositoryImpl @Inject constructor(
    private val remindersLocalDataSource: RemindersLocalDataSource,
    private val schedulers: Schedulers
) : RemindersRepository {

    override val remindersFlow = remindersLocalDataSource
        .remindersFlow
        .mapNotNull { reminder ->
            reminder?.toDomain()
        }

    override suspend fun getAllReminders(): List<ReminderDomain> = withContext(schedulers.io) {
        remindersLocalDataSource.getAllReminders()
            .orEmpty()
            .map { it.toDomain() }
    }

    override suspend fun saveReminder(reminderDomain: ReminderDomain) = withContext(schedulers.io) {
        remindersLocalDataSource.saveReminder(reminderDomain.toCached())
    }

    override suspend fun deleteReminderById(reminderId: Long) = withContext(schedulers.io) {
        remindersLocalDataSource.deleteReminderById(reminderId)
    }

    override suspend fun getReminderById(reminderId: Long): ReminderDomain? = withContext(schedulers.io) {
        remindersLocalDataSource.getReminderById(reminderId)?.toDomain()
    }
}
