package com.eyther.lumbridge.platform.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.forceTwoDecimalsPlaces
import com.eyther.lumbridge.model.loan.LoanCalculationUi
import com.eyther.lumbridge.model.loan.LoanUi
import com.eyther.lumbridge.model.recurringpayments.RecurringPaymentUi
import com.eyther.lumbridge.platform.notifications.LumbridgeNotificationSender
import com.eyther.lumbridge.shared.di.model.Schedulers
import com.eyther.lumbridge.usecase.loan.TryPayPendingLoanPaymentsUseCase
import com.eyther.lumbridge.usecase.recurringpayments.TryPayPendingRecurringPaymentsUseCase
import com.eyther.lumbridge.usecase.user.profile.GetLocaleOrDefault
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class CheckPendingPaymentsWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    private val tryPayPendingRecurringPaymentsUseCase: TryPayPendingRecurringPaymentsUseCase,
    private val tryPayPendingLoanPaymentsUseCase: TryPayPendingLoanPaymentsUseCase,
    private val notificationSender: LumbridgeNotificationSender,
    private val getLocaleOrDefault: GetLocaleOrDefault,
    private val schedulers: Schedulers,
    @Assisted workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val WORKER_TAG = "CheckPendingPaymentsWorker"
    }

    override suspend fun doWork(): Result = with(schedulers.io) {
        runCatching {
            Log.d(WORKER_TAG, "💬 Running CheckPendingPaymentsWorker")
            val paidRecurringPayments = tryPayPendingRecurringPaymentsUseCase()
            val paidLoans = tryPayPendingLoanPaymentsUseCase()
            val locale = getLocaleOrDefault()

            notifyUsers(paidRecurringPayments, paidLoans, locale.getCurrencySymbol())

            Result.success()
        }.getOrElse {
            Log.e(WORKER_TAG, "💥 Failed to run CheckPendingPaymentsWorker: $it")
            Result.failure()
        }
    }

    private fun notifyUsers(
        paidRecurringPayments: List<RecurringPaymentUi>,
        paidLoans: List<Pair<LoanUi, LoanCalculationUi>>,
        currencySymbol: String
    ) {
        if (paidRecurringPayments.isEmpty() && paidLoans.isEmpty()) {
            return
        }

        val title = appContext.getString(R.string.recurring_payment_notification)
        val messages = arrayListOf<String>()

        val combinedList =
            paidRecurringPayments.map { it.label to it.amountToPay } +
            paidLoans.map { it.first.name to it.second.monthlyPayment }

        combinedList.forEach { (label, amount) ->
            messages.add(
                appContext.getString(
                    R.string.recurring_payment_notification_message,
                    label, "${amount.forceTwoDecimalsPlaces()}$currencySymbol"
                )
            )
        }

        notificationSender.sendBatchNotification(
            title = title,
            messages = messages
        )
    }
}
