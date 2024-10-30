package com.eyther.lumbridge.extensions.platform

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import androidx.core.content.ContextCompat.getSystemService

fun Context.getNotificationManager() = getSystemService(this, NotificationManager::class.java)
fun Context.getAlarmManager() = getSystemService(this, AlarmManager::class.java)
