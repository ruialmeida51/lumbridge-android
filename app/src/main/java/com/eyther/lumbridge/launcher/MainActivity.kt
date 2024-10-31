package com.eyther.lumbridge.launcher

import android.content.res.Configuration
import android.os.Bundle
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
        WindowCompat.setDecorFitsSystemWindows(window, false)

        super.onCreate(savedInstanceState)

        notificationChannelBuilder.createNotificationChannel()

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
