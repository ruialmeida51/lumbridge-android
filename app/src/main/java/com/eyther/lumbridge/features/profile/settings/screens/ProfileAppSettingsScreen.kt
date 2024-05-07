package com.eyther.lumbridge.features.profile.settings.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.features.profile.settings.model.ProfileAppSettingsScreenViewState
import com.eyther.lumbridge.features.profile.settings.viewmodel.IProfileAppSettingsScreenViewModel
import com.eyther.lumbridge.features.profile.settings.viewmodel.ProfileAppAppSettingsScreenViewModel
import com.eyther.lumbridge.launcher.viewmodel.MainActivityViewModel
import com.eyther.lumbridge.ui.common.composables.components.setting.SwitchSetting
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.eyther.lumbridge.ui.theme.QuarterPadding
import com.eyther.lumbridge.ui.theme.runescapeTypography

@Composable
fun ProfileSettingsScreen(
    label: String,
    navController: NavHostController,
    viewModel: IProfileAppSettingsScreenViewModel = hiltViewModel<ProfileAppAppSettingsScreenViewModel>(),
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(DefaultPadding)
                .height(IntrinsicSize.Max)
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
private fun ColumnScope.Content(
    state: ProfileAppSettingsScreenViewState.Content,
    onDarkModeChange: (Boolean) -> Unit
) {
    Text(
        modifier = Modifier
            .padding(bottom = HalfPadding)
            .align(Alignment.Start),
        text = "Change your preferences",
        style = runescapeTypography.bodyLarge
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(8.dp))
            .shadow(elevation = QuarterPadding)
            .background(MaterialTheme.colorScheme.surfaceContainer)
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