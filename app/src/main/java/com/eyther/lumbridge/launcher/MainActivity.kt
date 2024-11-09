package com.eyther.lumbridge.launcher

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.eyther.lumbridge.launcher.screens.MainScreen
import com.eyther.lumbridge.launcher.viewmodel.IMainActivityViewModel
import com.eyther.lumbridge.launcher.viewmodel.MainActivityViewModel
import com.eyther.lumbridge.platform.notifications.LumbridgeNotificationChannelBuilder
import com.eyther.lumbridge.ui.theme.LumbridgeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var notificationChannelBuilder: LumbridgeNotificationChannelBuilder

    private val viewModel: IMainActivityViewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        notificationChannelBuilder.createNotificationChannel()
        checkAppSettings()

        setContent {
            val viewState = viewModel.viewState.collectAsStateWithLifecycle()
            val darkTheme = viewState.value.uiMode.isDarkTheme()

            LumbridgeTheme(darkTheme = darkTheme) {
                MainScreen()
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
