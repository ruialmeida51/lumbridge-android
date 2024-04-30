package com.eyther.lumbridge.launcher

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.eyther.lumbridge.launcher.model.UiMode
import com.eyther.lumbridge.ui.theme.LumbridgeTheme
import com.eyther.lumbridge.launcher.screens.MainScreen
import com.eyther.lumbridge.launcher.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkUserUiMode()

        setContent {
            val uiModeState = viewModel.uiMode.collectAsState()

            LumbridgeTheme(darkTheme = uiModeState.value.isDarkTheme()) {
                MainScreen(modifier = Modifier.clickable {
                    if (uiModeState.value.isDarkTheme()) {
                        viewModel.setLightMode()
                    } else {
                        viewModel.setDarkMode()
                    }
                })
            }
        }
    }

    private fun checkUserUiMode() {
        lifecycleScope.launch {
            val uiMode = viewModel.checkUserUiMode()

            when(uiMode) {
                UiMode.Dark -> viewModel.setDarkMode()
                UiMode.Light -> viewModel.setLightMode()
                null -> {
                    when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                        Configuration.UI_MODE_NIGHT_YES -> viewModel.setDarkMode()
                        Configuration.UI_MODE_NIGHT_NO -> viewModel.setLightMode()
                    }
                }
            }
        }
    }
}
