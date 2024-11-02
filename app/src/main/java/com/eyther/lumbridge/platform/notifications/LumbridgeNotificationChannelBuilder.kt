package com.eyther.lumbridge.platform.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.eyther.lumbridge.extensions.platform.getNotificationManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LumbridgeNotificationChannelBuilder @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val NOTIFICATION_DEFAULT_CHANNEL_ID = "lumbridge_notification_channel"
        const val NOTIFICATION_DEFAULT_CHANNEL_DESCRIPTION = "Lumbridge Notifications"

        const val NOTIFICATION_REMINDER_CHANNEL_ID = "lumbridge_reminder_channel"
        const val NOTIFICATION_REMINDER_CHANNEL_DESCRIPTION = "Lumbridge Reminders"

        const val NOTIFICATION_REPEATING_REMINDER_CHANNEL_ID = "lumbridge_repeating_reminder_channel"
        const val NOTIFICATION_REPEATING_REMINDER_CHANNEL_DESCRIPTION = "Lumbridge Repeating Reminders"

        const val NOTIFICATION_BATCH_CHANNEL_ID = "lumbridge_batch_channel"
        const val NOTIFICATION_BATCH_CHANNEL_DESCRIPTION = "Lumbridge Batch Notifications"

        const val NOTIFICATION_OPEN_ACTIVITY_INTENT_REQUEST_CODE = 0
        const val NOTIFICATION_DEFAULT_INTENT_REQUEST_CODE = 1
        const val NOTIFICATION_REMINDER_INTENT_REQUEST_CODE = 2
        const val NOTIFICATION_REPEATING_REMINDER_INTENT_REQUEST_CODE = 3
        const val NOTIFICATION_BATCH_INTENT_REQUEST_CODE = 4

        const val NOTIFICATION_ARG_TITLE = "arg_notification_title"
        const val NOTIFICATION_ARG_MESSAGE = "arg_notification_message"
    }

    fun createNotificationChannel() {
        val notificationManager = context.getNotificationManager() ?: return

        val defaultChannel = NotificationChannel(
            NOTIFICATION_DEFAULT_CHANNEL_ID,
            NOTIFICATION_DEFAULT_CHANNEL_DESCRIPTION,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val reminderChannel = NotificationChannel(
            NOTIFICATION_REMINDER_CHANNEL_ID,
            NOTIFICATION_REMINDER_CHANNEL_DESCRIPTION,
            NotificationManager.IMPORTANCE_HIGH
        )

        val repeatingReminderChannel = NotificationChannel(
            NOTIFICATION_REPEATING_REMINDER_CHANNEL_ID,
            NOTIFICATION_REPEATING_REMINDER_CHANNEL_DESCRIPTION,
            NotificationManager.IMPORTANCE_HIGH
        )

        val batchChannel = NotificationChannel(
            NOTIFICATION_BATCH_CHANNEL_ID,
            NOTIFICATION_BATCH_CHANNEL_DESCRIPTION,
            NotificationManager.IMPORTANCE_HIGH
        )

        notificationManager.createNotificationChannel(defaultChannel)
        notificationManager.createNotificationChannel(reminderChannel)
        notificationManager.createNotificationChannel(repeatingReminderChannel)
        notificationManager.createNotificationChannel(batchChannel)
    }
}
