package com.eyther.lumbridge.features.profile.settings.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.features.profile.settings.model.ProfileAppSettingsScreenViewState
import com.eyther.lumbridge.features.profile.settings.viewmodel.ProfileAppAppSettingsScreenViewModel
import com.eyther.lumbridge.features.profile.settings.viewmodel.ProfileAppSettingsScreenViewModelInterface
import com.eyther.lumbridge.launcher.viewmodel.MainActivityViewModel
import com.eyther.lumbridge.ui.common.composables.components.setting.SwitchSetting
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding

@Composable
fun ProfileSettingsScreen(
    label: String,
    navController: NavHostController,
    viewModel: ProfileAppSettingsScreenViewModelInterface = hiltViewModel<ProfileAppAppSettingsScreenViewModel>(),
    parentViewModel: MainActivityViewModel = hiltViewModel()
) {
    val state = viewModel.viewState.collectAsState().value

    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                topAppBarVariation = TopAppBarVariation.TitleAndIcon(title = label) {
                    navController.popBackStack()
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            when (state) {
                is ProfileAppSettingsScreenViewState.Content -> Content(
                    state = state,
                    onDarkModeChange = parentViewModel::toggleDarkMode
                )

                is ProfileAppSettingsScreenViewState.Loading -> Unit
            }
        }
    }
}

@Composable
private fun Content(
    state: ProfileAppSettingsScreenViewState.Content,
    onDarkModeChange: (Boolean) -> Unit
) {
    Column {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(DefaultPadding),
            shape = RoundedCornerShape(8),
        ) {

            Column(
                modifier = Modifier
                    .padding(DefaultPadding),
                verticalArrangement = Arrangement.spacedBy(DefaultPadding)
            ) {
                SwitchSetting(
                    icon = R.drawable.ic_sun,
                    label = "Dark Mode",
                    isChecked = state.isDarkModeEnabled ?: isSystemInDarkTheme(),
                    onCheckedChange = { onDarkModeChange(it) }
                )
            }
        }
    }
}