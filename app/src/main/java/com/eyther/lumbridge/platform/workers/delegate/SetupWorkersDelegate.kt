package com.eyther.lumbridge.platform.workers.delegate

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.eyther.lumbridge.platform.workers.CheckPendingPaymentsWorker
import com.eyther.lumbridge.platform.workers.CheckPendingRemindersWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Duration
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SetupWorkersDelegate @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    companion object {
        private const val TAG = "SetupWorkersDelegate"
    }

    fun setupWorkers() = runCatching {
        setupRecurringPaymentsWorker()
        setupRemindersWorker()
    }.getOrElse {
        Log.e(TAG, "💥 Failed to setup workers: $it")
    }

    private fun setupRecurringPaymentsWorker() {
        Log.d(TAG, "💬 Setting up recurring payments worker")

        val workerTag = "CheckPendingPaymentsWork"

        // Calculate the initial delay to run at midnight
        val currentTime = LocalTime.now()
        val midnight = LocalTime.MIDNIGHT

        // If the current time is after midnight, calculate the delay until the next midnight.
        val initialDelay = if (currentTime.isAfter(midnight)) {
            Duration.between(currentTime, midnight.plusHours(24))
        } else {
            Duration.between(currentTime, midnight)
        }

        val constraints = Constraints.Builder()
            .setRequiresStorageNotLow(true)
            .build()

        // Run the worker every day at midnight
        val workRequest = PeriodicWorkRequestBuilder<CheckPendingPaymentsWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(initialDelay.toMillis(), TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                workerTag,
                ExistingPeriodicWorkPolicy.UPDATE,
                workRequest
            )

        Log.d(TAG, "🚀 Recurring payments worker set up")
    }

    private fun setupRemindersWorker() {
        Log.d(TAG, "💬 Setting up reminders worker")

        val workerTag = "CheckPendingRemindersWork"

        // Run the worker in 15 minute intervals
        val workRequest = PeriodicWorkRequestBuilder<CheckPendingRemindersWorker>(15, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                workerTag,
                ExistingPeriodicWorkPolicy.UPDATE,
                workRequest
            )

        Log.d(TAG, "🚀 Reminders worker set up")
    }
}
