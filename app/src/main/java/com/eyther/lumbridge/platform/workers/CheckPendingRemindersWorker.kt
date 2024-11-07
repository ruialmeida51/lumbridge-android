package com.eyther.lumbridge.platform.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.eyther.lumbridge.model.reminders.ReminderUi
import com.eyther.lumbridge.platform.notifications.LumbridgeNotificationSender
import com.eyther.lumbridge.shared.di.model.Schedulers
import com.eyther.lumbridge.shared.time.extensions.isBeforeOrEqual
import com.eyther.lumbridge.shared.time.extensions.toDayMonthYearHourMinuteString
import com.eyther.lumbridge.usecase.reminders.GetRemindersUseCase
import com.eyther.lumbridge.usecase.reminders.SetReminderAsNotifiedUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDateTime

@HiltWorker
class CheckPendingRemindersWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    private val getRemindersUseCase: GetRemindersUseCase,
    private val setReminderAsNotifiedUseCase: SetReminderAsNotifiedUseCase,
    private val notificationSender: LumbridgeNotificationSender,
    private val schedulers: Schedulers,
    @Assisted workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val WORKER_TAG = "CheckPendingPaymentsWorker"
    }

    override suspend fun doWork(): Result = with(schedulers.io) {
        runCatching {
            Log.d(WORKER_TAG, "💬 Running CheckPendingPaymentsWorker")

            val reminders = getRemindersUseCase()
            val now = LocalDateTime.now()

            val remindersToNotify = reminders
                .filter { it.remindMeIn.reminderTime?.isBeforeOrEqual(now) == true && !it.alreadyNotified }

            notifyUsers(remindersToNotify)

            Result.success()
        }.getOrElse {
            Log.e(WORKER_TAG, "💥 Failed to run CheckPendingPaymentsWorker: $it")
            Result.failure()
        }
    }

    private suspend fun notifyUsers(reminders: List<ReminderUi>) {
        if (reminders.isEmpty()) {
            return
        }

        reminders.forEach { reminder ->
            val title = reminder.label
            val message = reminder.dueDate.toDayMonthYearHourMinuteString()

            notificationSender.sendReminderNotification(
                title = title,
                message = message
            )

            setReminderAsNotifiedUseCase(reminder)
        }
    }
}
