package com.eyther.lumbridge.platform.notifications

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.platform.getLaunchMainActivityIntent
import com.eyther.lumbridge.extensions.platform.getNotificationManager
import com.eyther.lumbridge.platform.notifications.LumbridgeNotificationChannelBuilder.Companion.NOTIFICATION_ARG_MESSAGE
import com.eyther.lumbridge.platform.notifications.LumbridgeNotificationChannelBuilder.Companion.NOTIFICATION_ARG_TITLE
import com.eyther.lumbridge.platform.notifications.LumbridgeNotificationChannelBuilder.Companion.NOTIFICATION_BATCH_CHANNEL_ID
import com.eyther.lumbridge.platform.notifications.LumbridgeNotificationChannelBuilder.Companion.NOTIFICATION_OPEN_ACTIVITY_INTENT_REQUEST_CODE
import java.util.UUID

class LumbridgeBatchNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager = context?.getNotificationManager() ?: return
        val nonNullIntent = intent ?: return

        Log.d("LumbridgeBatchNotificationReceiver", "onReceive: $nonNullIntent")

        val title = nonNullIntent.getStringExtra(NOTIFICATION_ARG_TITLE) ?: return
        val messages = nonNullIntent.getStringArrayListExtra(NOTIFICATION_ARG_MESSAGE) ?: return

        val contentIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_OPEN_ACTIVITY_INTENT_REQUEST_CODE,
            context.getLaunchMainActivityIntent(),
            PendingIntent.FLAG_IMMUTABLE
        )

        // Create an InboxStyle notification, used to display multiple messages
        val inboxStyle = NotificationCompat.InboxStyle()
            .setSummaryText(context.getString(R.string.recurring_payment_notification_summary, messages.size))

        // Add each message to the InboxStyle notification.
        messages.forEach { inboxStyle.addLine(it) }

        val notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_BATCH_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_yarn)
            .setContentTitle(title)
            .setStyle(inboxStyle)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(contentIntent)

        notificationManager.notify(
            UUID.randomUUID().hashCode() + (title + messages).hashCode(),
            notificationBuilder.build()
        )
    }
}