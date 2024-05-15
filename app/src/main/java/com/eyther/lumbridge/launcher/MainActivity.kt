package com.eyther.lumbridge.launcher

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.eyther.lumbridge.launcher.screens.MainScreen
import com.eyther.lumbridge.launcher.viewmodel.IMainActivityViewModel
import com.eyther.lumbridge.launcher.viewmodel.MainActivityViewModel
import com.eyther.lumbridge.ui.theme.LumbridgeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: IMainActivityViewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkThemeSettings()

        setContent {
            val viewState = viewModel.viewState.collectAsStateWithLifecycle()

            LumbridgeTheme(darkTheme = viewState.value.uiMode.isDarkTheme()) {
                MainScreen()
            }
        }
    }

    private fun checkThemeSettings() {
        lifecycleScope.launch {
            if (!viewModel.hasStoredPreferences()) {
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> viewModel.toggleDarkMode(isDarkMode = true)
                    Configuration.UI_MODE_NIGHT_NO -> viewModel.toggleDarkMode(isDarkMode = false)
                }
            }
        }
    }
}
