package com.eyther.lumbridge.platform.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.eyther.lumbridge.model.loan.LoanUi
import com.eyther.lumbridge.model.recurringpayments.RecurringPaymentUi
import com.eyther.lumbridge.platform.notifications.LumbridgeDefaultNotificationReceiver
import com.eyther.lumbridge.platform.notifications.LumbridgeNotificationScheduler
import com.eyther.lumbridge.shared.di.model.Schedulers
import com.eyther.lumbridge.usecase.loan.TryPayPendingLoanPaymentsUseCase
import com.eyther.lumbridge.usecase.recurringpayments.TryPayPendingRecurringPaymentsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.forceTwoDecimalsPlaces
import com.eyther.lumbridge.model.loan.LoanCalculationUi
import com.eyther.lumbridge.usecase.user.profile.GetLocaleOrDefault

@HiltWorker
class CheckPendingPaymentsWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    private val tryPayPendingRecurringPaymentsUseCase: TryPayPendingRecurringPaymentsUseCase,
    private val tryPayPendingLoanPaymentsUseCase: TryPayPendingLoanPaymentsUseCase,
    private val notificationScheduler: LumbridgeNotificationScheduler,
    private val getLocaleOrDefault: GetLocaleOrDefault,
    private val schedulers: Schedulers,
    @Assisted workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val WORKER_TAG = "CheckPendingPaymentsWorker"
    }

    override suspend fun doWork(): Result = with(schedulers.io) {
        runCatching {
            Log.d(WORKER_TAG, "\uD83D\uDCAC Running CheckPendingPaymentsWorker")
            val paidRecurringPayments = tryPayPendingRecurringPaymentsUseCase()
            val paidLoans = tryPayPendingLoanPaymentsUseCase()
            val locale = getLocaleOrDefault()

            notifyUserOfPaidRecurringPayments(paidRecurringPayments, locale.getCurrencySymbol())
            notifyUserOfPaidLoans(paidLoans, locale.getCurrencySymbol())

            Result.success()
        }.getOrElse {
            Log.e(WORKER_TAG, "\uD83D\uDCA5 Failed to run CheckPendingPaymentsWorker: $it")
            Result.failure()
        }
    }

    private fun notifyUserOfPaidRecurringPayments(
        paidRecurringPayments: List<RecurringPaymentUi>,
        currencySymbol: String
    ) {
        paidRecurringPayments.forEach { payment ->
            notificationScheduler.scheduleDefaultNotification(
                title = appContext.getString(R.string.recurring_payment_notification),
                message = appContext.getString(
                    R.string.recurring_payment_notification_message,
                    payment.label,
                    "${payment.amountToPay.forceTwoDecimalsPlaces()}$currencySymbol"
                ),
                whenToDisplayInMillis = System.currentTimeMillis()
            )
        }
    }

    private fun notifyUserOfPaidLoans(
        paidLoans: List<Pair<LoanUi, LoanCalculationUi>>,
        currencySymbol: String
    ) {
        paidLoans.forEach { (loan, loanCalculation) ->
            notificationScheduler.scheduleDefaultNotification(
                title = appContext.getString(R.string.recurring_payment_notification),
                message = appContext.getString(
                    R.string.recurring_payment_notification_message,
                    loan.name,
                    "${loanCalculation.monthlyPayment.forceTwoDecimalsPlaces()}$currencySymbol"
                ),
                whenToDisplayInMillis = System.currentTimeMillis()
            )
        }
    }
}
