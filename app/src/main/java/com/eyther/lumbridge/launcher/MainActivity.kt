package com.eyther.lumbridge.launcher

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.forceTwoDecimalsPlaces
import com.eyther.lumbridge.launcher.screens.MainScreen
import com.eyther.lumbridge.launcher.viewmodel.IMainActivityViewModel
import com.eyther.lumbridge.launcher.viewmodel.MainActivityViewModel
import com.eyther.lumbridge.platform.notifications.LumbridgeNotificationChannelBuilder
import com.eyther.lumbridge.platform.notifications.LumbridgeNotificationScheduler
import com.eyther.lumbridge.ui.theme.LumbridgeTheme
import com.eyther.lumbridge.usecase.loan.TryPayPendingLoanPaymentsUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var notificationChannelBuilder: LumbridgeNotificationChannelBuilder

    @Inject
    lateinit var tryPayPendingLoans: TryPayPendingLoanPaymentsUseCase

    @Inject
    lateinit var notificationScheduler: LumbridgeNotificationScheduler

    private val viewModel: IMainActivityViewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)

        super.onCreate(savedInstanceState)

        notificationChannelBuilder.createNotificationChannel()

        lifecycleScope.launch {
            val loans = tryPayPendingLoans()

            loans.forEach { (loan, loanCalculation) ->
                Log.d("MainActivity", "Loan paid: ${loan.name}")
                notificationScheduler.scheduleDefaultNotification(
                    title = getString(R.string.recurring_payment_notification),
                    message = getString(R.string.recurring_payment_notification_message, loan.name, loanCalculation.monthlyPayment.forceTwoDecimalsPlaces()),
                    whenToDisplayInMillis = System.currentTimeMillis()
                )
            }
        }

        checkAppSettings()

        setContent {
            val viewState = viewModel.viewState.collectAsStateWithLifecycle()

            Box(
                Modifier
                    .padding(WindowInsets.systemBars.asPaddingValues())
            ) {
                Box(
                    Modifier
                        .consumeWindowInsets(WindowInsets.systemBars)
                ) {
                    LumbridgeTheme(darkTheme = viewState.value.uiMode.isDarkTheme()) {
                        MainScreen()
                    }
                }
            }
        }
    }

    private fun checkAppSettings() {
        lifecycleScope.launch {
            if (viewModel.hasStoredPreferences()) {
                viewModel.updateSettings()
            } else {
                val isDarkMode = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> true
                    else -> false
                }

                viewModel.updateSettings(isDarkMode)
            }
        }
    }
}
