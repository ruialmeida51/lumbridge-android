package com.eyther.lumbridge.data.datasource.reminders.local

import com.eyther.lumbridge.data.datasource.reminders.dao.RemindersDao
import com.eyther.lumbridge.data.mappers.reminders.toCached
import com.eyther.lumbridge.data.mappers.reminders.toEntity
import com.eyther.lumbridge.data.model.reminders.local.ReminderCached
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RemindersLocalDataSource @Inject constructor(
    private val remindersDao: RemindersDao
) {
    val remindersFlow = remindersDao
        .getRemindersFlow()
        .map { flowItem ->
            flowItem?.map { reminderEntity -> reminderEntity.toCached() }
        }

    suspend fun getAllReminders(): List<ReminderCached>? {
        return remindersDao.getAllReminders()
            ?.map { reminderEntity -> reminderEntity.toCached() }
    }

    suspend fun saveReminder(reminderCached: ReminderCached) {
        if (reminderCached.reminderId == -1L) {
            remindersDao.insertReminder(reminderCached.toEntity())
        } else {
            remindersDao.updateReminder(
                reminderCached.toEntity().copy(reminderId = reminderCached.reminderId)
            )
        }
    }

    suspend fun deleteReminderById(reminderId: Long) {
        remindersDao.deleteReminderById(reminderId)
    }

    suspend fun getReminderById(reminderId: Long): ReminderCached? {
        return remindersDao.getReminderById(reminderId)?.toCached()
    }

}
