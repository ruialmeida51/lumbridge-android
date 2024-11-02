package com.eyther.lumbridge.platform.notifications

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.platform.getLaunchMainActivityIntent
import com.eyther.lumbridge.extensions.platform.getNotificationManager
import com.eyther.lumbridge.platform.notifications.LumbridgeNotificationChannelBuilder.Companion.NOTIFICATION_ARG_MESSAGE
import com.eyther.lumbridge.platform.notifications.LumbridgeNotificationChannelBuilder.Companion.NOTIFICATION_ARG_TITLE
import com.eyther.lumbridge.platform.notifications.LumbridgeNotificationChannelBuilder.Companion.NOTIFICATION_DEFAULT_CHANNEL_ID
import com.eyther.lumbridge.platform.notifications.LumbridgeNotificationChannelBuilder.Companion.NOTIFICATION_DEFAULT_INTENT_REQUEST_CODE
import com.eyther.lumbridge.platform.notifications.LumbridgeNotificationChannelBuilder.Companion.NOTIFICATION_OPEN_ACTIVITY_INTENT_REQUEST_CODE

class LumbridgeDefaultNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager = context?.getNotificationManager() ?: return
        val nonNullIntent = intent ?: return

        val title = nonNullIntent.getStringExtra(NOTIFICATION_ARG_TITLE)
        val message = nonNullIntent.getStringExtra(NOTIFICATION_ARG_MESSAGE)

        val contentIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_OPEN_ACTIVITY_INTENT_REQUEST_CODE,
            context.getLaunchMainActivityIntent(),
            PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_DEFAULT_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_yarn)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(contentIntent)

        notificationManager.notify(
            NOTIFICATION_DEFAULT_INTENT_REQUEST_CODE,
            notificationBuilder.build()
        )
    }
}