package com.eyther.lumbridge.data.datasource.reminders.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.eyther.lumbridge.data.model.reminders.entity.REMINDERS_TABLE_NAME
import com.eyther.lumbridge.data.model.reminders.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RemindersDao {
    @Transaction
    @Query("SELECT * FROM $REMINDERS_TABLE_NAME")
    fun getRemindersFlow(): Flow<List<ReminderEntity>?>

    @Transaction
    @Query("SELECT * FROM $REMINDERS_TABLE_NAME")
    suspend fun getAllReminders(): List<ReminderEntity>?

    @Transaction
    @Query("SELECT * FROM $REMINDERS_TABLE_NAME WHERE reminderId = :reminderId")
    suspend fun getReminderById(reminderId: Long): ReminderEntity?

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderEntity): Long

    @Transaction
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateReminder(reminder: ReminderEntity)

    @Transaction
    @Query("DELETE FROM $REMINDERS_TABLE_NAME WHERE reminderId = :reminderId")
    suspend fun deleteReminderById(reminderId: Long)
}
