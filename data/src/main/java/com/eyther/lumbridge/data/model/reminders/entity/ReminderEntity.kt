package com.eyther.lumbridge.data.model.reminders.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

const val REMINDERS_TABLE_NAME = "reminders"

@Entity(
    tableName = REMINDERS_TABLE_NAME
)
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true) val reminderId: Long = 0,
    val label: String,
    val dueDate: String,
    val remindMeInOrdinal: Int,
    val remindMeInDaysCustomValue: Int?,
    val remindMeInHoursCustomValue: Int?,
    val remindMeInMinutesCustomValue: Int?,
    val alreadyNotified: Boolean
)
