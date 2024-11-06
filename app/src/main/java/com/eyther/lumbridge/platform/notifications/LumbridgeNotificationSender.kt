package com.eyther.lumbridge.platform.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.eyther.lumbridge.platform.notifications.LumbridgeNotificationChannelBuilder.Companion.NOTIFICATION_BATCH_INTENT_REQUEST_CODE
import com.eyther.lumbridge.platform.notifications.LumbridgeNotificationChannelBuilder.Companion.NOTIFICATION_DEFAULT_INTENT_REQUEST_CODE
import com.eyther.lumbridge.platform.notifications.LumbridgeNotificationChannelBuilder.Companion.NOTIFICATION_REMINDER_INTENT_REQUEST_CODE
import com.eyther.lumbridge.platform.notifications.LumbridgeNotificationChannelBuilder.Companion.NOTIFICATION_REPEATING_REMINDER_INTENT_REQUEST_CODE
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LumbridgeNotificationSender @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun scheduleReminderNotification(
        title: String,
        message: String,
    ) {
        createPendingIntent(
            requestCode = NOTIFICATION_REMINDER_INTENT_REQUEST_CODE,
            title = title,
            message = message,
            receiver = LumbridgeReminderNotificationReceiver::class.java
        ).send()
    }

    fun scheduleDefaultNotification(
        title: String,
        message: String
    ) {
        createPendingIntent(
            requestCode = NOTIFICATION_DEFAULT_INTENT_REQUEST_CODE,
            title = title,
            message = message,
            receiver = LumbridgeDefaultNotificationReceiver::class.java
        ).send()
    }

    fun scheduleRepeatingReminderNotification(
        title: String,
        message: String,
        timeBetweenDisplaysInMillis: Long
    ) {
        if (timeBetweenDisplaysInMillis < 60000) {
            throw IllegalArgumentException("\uD83D\uDCA5 Repeating reminder notifications must be at least 1 minute apart.")
        }

        createPendingIntent(
            requestCode = NOTIFICATION_REPEATING_REMINDER_INTENT_REQUEST_CODE,
            title = title,
            message = message,
            receiver = LumbridgeRepeatingReminderNotificationReceiver::class.java,
            flags = PendingIntent.FLAG_MUTABLE
        ).send()
    }

    fun sendBatchNotification(
        title: String,
        messages: ArrayList<String>
    ) {
        createBatchPendingIntent(
            requestCode = NOTIFICATION_BATCH_INTENT_REQUEST_CODE,
            title = title,
            messages = messages,
            receiver = LumbridgeBatchNotificationReceiver::class.java
        ).send()
    }

    private fun createPendingIntent(
        requestCode: Int,
        title: String,
        message: String,
        receiver: Class<*>,
        flags: Int = PendingIntent.FLAG_IMMUTABLE
    ): PendingIntent {
        val intent = Intent(context, receiver).apply {
            putExtra(LumbridgeNotificationChannelBuilder.NOTIFICATION_ARG_TITLE, title)
            putExtra(LumbridgeNotificationChannelBuilder.NOTIFICATION_ARG_MESSAGE, message)
        }

        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            flags
        )
    }

    private fun createBatchPendingIntent(
        requestCode: Int,
        title: String,
        messages: ArrayList<String>,
        receiver: Class<*>,
        flags: Int = PendingIntent.FLAG_IMMUTABLE
    ): PendingIntent {
        val intent = Intent(context, receiver).apply {
            putExtra(LumbridgeNotificationChannelBuilder.NOTIFICATION_ARG_TITLE, title)
            putStringArrayListExtra(LumbridgeNotificationChannelBuilder.NOTIFICATION_ARG_MESSAGE, messages)
        }

        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            flags
        )
    }
}
