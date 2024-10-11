package com.eyther.lumbridge.features.profile.settings.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.domain.model.locale.SupportedLanguages
import com.eyther.lumbridge.extensions.platform.changeAppLanguage
import com.eyther.lumbridge.features.profile.settings.model.ProfileAppSettingsScreenViewEffect
import com.eyther.lumbridge.features.profile.settings.model.ProfileAppSettingsScreenViewState
import com.eyther.lumbridge.features.profile.settings.viewmodel.IProfileAppSettingsScreenViewModel
import com.eyther.lumbridge.features.profile.settings.viewmodel.ProfileAppAppSettingsScreenViewModel
import com.eyther.lumbridge.ui.common.composables.components.card.ColumnCardWrapper
import com.eyther.lumbridge.ui.common.composables.components.input.DropdownInput
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.setting.SwitchSetting
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun ProfileSettingsScreen(
    @StringRes label: Int,
    navController: NavHostController,
    viewModel: IProfileAppSettingsScreenViewModel = hiltViewModel<ProfileAppAppSettingsScreenViewModel>()
) {
    val state = viewModel.viewState.collectAsStateWithLifecycle().value
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.viewEffects
                .onEach { viewEffect ->
                    when (viewEffect) {
                        is ProfileAppSettingsScreenViewEffect.ChangeAppLanguage -> changeAppLanguage(viewEffect.appLanguageCountryCode)
                    }
                }
                .collect()
        }
    }

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
        ) {
            when (state) {
                is ProfileAppSettingsScreenViewState.Content -> Content(
                    state = state,
                    onDarkModeChange = viewModel::onDarkModeChanged,
                    onLanguageChanged = viewModel::onAppLanguageChanged
                )

                is ProfileAppSettingsScreenViewState.Loading -> LoadingIndicator()
            }
        }
    }
}

@Composable
private fun ColumnScope.Content(
    state: ProfileAppSettingsScreenViewState.Content,
    onDarkModeChange: (Boolean) -> Unit,
    onLanguageChanged: (String) -> Unit
) {
    Text(
        modifier = Modifier
            .padding(start = DefaultPadding, end = DefaultPadding, bottom = HalfPadding)
            .align(Alignment.Start),
        text = stringResource(id = R.string.app_settings_change_preferences),
        style = MaterialTheme.typography.bodyLarge
    )

    ColumnCardWrapper(
        verticalArrangement = Arrangement.spacedBy(DefaultPadding)
    ) {
        SwitchSetting(
            icon = R.drawable.ic_sun,
            label = stringResource(id = R.string.dark_mode),
            isChecked = state.inputState.isDarkMode,
            onCheckedChange = { onDarkModeChange(it) }
        )
    }

    Spacer(modifier = Modifier.height(DefaultPadding))

    ColumnCardWrapper {
        DropdownInput(
            label = stringResource(id = R.string.app_settings_change_language),
            selectedOption = stringResource(state.inputState.appLanguage.getStringResource()),
            items = state.availableLanguages.map { it.countryCode to stringResource(it.getStringResource()) },
            onItemClick = { countryCode, _ -> onLanguageChanged(countryCode) }
        )
    }
}

@StringRes
private fun SupportedLanguages.getStringResource() = when (this) {
    SupportedLanguages.PORTUGUESE -> R.string.language_portuguese
    SupportedLanguages.ENGLISH -> R.string.language_english
}
