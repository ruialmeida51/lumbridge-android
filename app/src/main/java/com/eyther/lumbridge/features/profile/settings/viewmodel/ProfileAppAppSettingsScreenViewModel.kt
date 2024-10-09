package com.eyther.lumbridge.features.profile.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.profile.settings.model.ProfileAppSettingsScreenViewEffect
import com.eyther.lumbridge.features.profile.settings.model.ProfileAppSettingsScreenViewState
import com.eyther.lumbridge.features.profile.settings.viewmodel.delegate.IProfileAppSettingsScreenInputHandler
import com.eyther.lumbridge.features.profile.settings.viewmodel.delegate.ProfileAppSettingsScreenInputHandler
import com.eyther.lumbridge.usecase.locale.GetSupportedLanguages
import com.eyther.lumbridge.usecase.preferences.GetPreferencesFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileAppAppSettingsScreenViewModel @Inject constructor(
    private val getPreferencesFlow: GetPreferencesFlow,
    private val getSupportedLanguages: GetSupportedLanguages,
    private val profileAppSettingsScreenInputHandler: ProfileAppSettingsScreenInputHandler
) : ViewModel(),
    IProfileAppSettingsScreenViewModel,
    IProfileAppSettingsScreenInputHandler by profileAppSettingsScreenInputHandler {

    override val viewState: MutableStateFlow<ProfileAppSettingsScreenViewState> =
        MutableStateFlow(ProfileAppSettingsScreenViewState.Loading)

    override val viewEffects: MutableSharedFlow<ProfileAppSettingsScreenViewEffect> =
        MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    init {
        observeChanges()
    }

    private fun observeChanges() {
        viewModelScope.launch {
            val preferences = checkNotNull(getPreferencesFlow().firstOrNull()) {
                "Preferences should not be null when viewing the app settings screen."
            }

            updateInput {
                it.copy(
                    isDarkMode = preferences.isDarkMode,
                    appLanguage = preferences.appLanguage
                )
            }

            inputState
                .onEach { inputState ->
                    viewState.update {
                        ProfileAppSettingsScreenViewState.Content(
                            inputState = inputState,
                            availableLanguages = getSupportedLanguages()
                        )
                    }
                }
                .launchIn(this)

            viewState
                .filterIsInstance<ProfileAppSettingsScreenViewState.Content>()
                .onEach { contentState ->
                    viewEffects.emit(
                        ProfileAppSettingsScreenViewEffect.UpdateAppSettings(
                            isDarkMode = contentState.inputState.isDarkMode,
                            appLanguageCountryCode = contentState.inputState.appLanguage.countryCode
                        )
                    )
                }.launchIn(this)
        }
    }
}
