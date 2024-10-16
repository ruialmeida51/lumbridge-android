package com.eyther.lumbridge.features.profile.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.domain.model.preferences.Preferences
import com.eyther.lumbridge.features.profile.settings.model.ProfileAppSettingsScreenInputState
import com.eyther.lumbridge.features.profile.settings.model.ProfileAppSettingsScreenViewEffect
import com.eyther.lumbridge.features.profile.settings.model.ProfileAppSettingsScreenViewState
import com.eyther.lumbridge.features.profile.settings.viewmodel.delegate.IProfileAppSettingsScreenInputHandler
import com.eyther.lumbridge.features.profile.settings.viewmodel.delegate.ProfileAppSettingsScreenInputHandler
import com.eyther.lumbridge.usecase.locale.GetSupportedLanguages
import com.eyther.lumbridge.usecase.preferences.GetPreferencesFlow
import com.eyther.lumbridge.usecase.preferences.SavePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileAppSettingsScreenViewModel @Inject constructor(
    private val getPreferencesFlow: GetPreferencesFlow,
    private val getSupportedLanguages: GetSupportedLanguages,
    private val savePreferences: SavePreferences,
    private val profileAppSettingsScreenInputHandler: ProfileAppSettingsScreenInputHandler
) : ViewModel(),
    IProfileAppSettingsScreenViewModel,
    IProfileAppSettingsScreenInputHandler by profileAppSettingsScreenInputHandler {

    private var currentPreferences: Preferences? = null

    override val viewState: MutableStateFlow<ProfileAppSettingsScreenViewState> =
        MutableStateFlow(ProfileAppSettingsScreenViewState.Loading)

    override val viewEffects: MutableSharedFlow<ProfileAppSettingsScreenViewEffect> =
        MutableSharedFlow()

    init {
        observeChanges()
    }

    private fun observeChanges() {
        viewModelScope.launch {
            val preferences = checkNotNull(getPreferencesFlow().firstOrNull()) {
                "Preferences should not be null when viewing the app settings screen."
            }

            currentPreferences = preferences

            updateInput {
                it.copy(
                    isDarkMode = preferences.isDarkMode,
                    appLanguage = preferences.appLanguage
                )
            }

            inputState
                .onEach { updateContentState(it) }
                .onEach { handleViewEffects(it) }
                .onEach { updateSettings(it) }
                .launchIn(this)
        }
    }

    private fun updateContentState(inputState: ProfileAppSettingsScreenInputState) {
        viewState.update {
            ProfileAppSettingsScreenViewState.Content(
                inputState = inputState,
                availableLanguages = getSupportedLanguages()
            )
        }
    }

    private suspend fun handleViewEffects(inputState: ProfileAppSettingsScreenInputState) {
        val newLanguage = inputState.appLanguage
        val oldLanguage = currentPreferences?.appLanguage

        if (newLanguage != oldLanguage) {
            viewEffects.emit(ProfileAppSettingsScreenViewEffect.ChangeAppLanguage(newLanguage.countryCode))
            currentPreferences = currentPreferences?.copy(appLanguage = newLanguage)
        }
    }

    private suspend fun updateSettings(inputState: ProfileAppSettingsScreenInputState) {
        val newDarkMode = inputState.isDarkMode
        val newAppLanguage = inputState.appLanguage

        savePreferences(
            isDarkMode = newDarkMode,
            appLanguages = newAppLanguage
        )
    }
}
