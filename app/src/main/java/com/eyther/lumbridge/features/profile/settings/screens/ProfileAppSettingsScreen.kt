package com.eyther.lumbridge.features.profile.settings.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.features.profile.settings.model.ProfileAppSettingsScreenViewState
import com.eyther.lumbridge.features.profile.settings.viewmodel.IProfileAppSettingsScreenViewModel
import com.eyther.lumbridge.features.profile.settings.viewmodel.ProfileAppAppSettingsScreenViewModel
import com.eyther.lumbridge.launcher.viewmodel.MainActivityViewModel
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.setting.SwitchSetting
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.DefaultRoundedCorner
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.eyther.lumbridge.ui.theme.QuarterPadding

@Composable
fun ProfileSettingsScreen(
    @StringRes label: Int,
    navController: NavHostController,
    viewModel: IProfileAppSettingsScreenViewModel = hiltViewModel<ProfileAppAppSettingsScreenViewModel>(),
    parentViewModel: MainActivityViewModel = hiltViewModel()
) {
    val state = viewModel.viewState.collectAsStateWithLifecycle().value

    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                topAppBarVariation = TopAppBarVariation.TitleAndIcon(
                    title = stringResource(id = label)
                ) {
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
        ) {
            when (state) {
                is ProfileAppSettingsScreenViewState.Content -> Content(
                    state = state,
                    onDarkModeChange = parentViewModel::toggleDarkMode
                )

                is ProfileAppSettingsScreenViewState.Loading -> LoadingIndicator()
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
        text = stringResource(id = R.string.app_settings_change_preferences),
        style = MaterialTheme.typography.bodyLarge
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(DefaultRoundedCorner))
            .shadow(elevation = QuarterPadding)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(DefaultPadding),
        verticalArrangement = Arrangement.spacedBy(DefaultPadding)
    ) {
        SwitchSetting(
            icon = R.drawable.ic_sun,
            label = stringResource(id = R.string.dark_mode),
            isChecked = state.isDarkModeEnabled ?: isSystemInDarkTheme(),
            onCheckedChange = { onDarkModeChange(it) }
        )
    }
}