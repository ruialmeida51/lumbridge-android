package com.eyther.lumbridge.launcher.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.domain.model.locale.SupportedLanguages
import com.eyther.lumbridge.launcher.model.MainScreenViewState
import com.eyther.lumbridge.launcher.model.UiMode
import com.eyther.lumbridge.usecase.locale.GetCurrentSystemLanguageOrDefault
import com.eyther.lumbridge.usecase.preferences.GetPreferencesFlow
import com.eyther.lumbridge.usecase.preferences.SavePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getPreferencesFlow: GetPreferencesFlow,
    private val savePreferences: SavePreferences,
    private val getCurrentSystemLanguageOrDefault: GetCurrentSystemLanguageOrDefault
) : ViewModel(), IMainActivityViewModel {

    override val viewState = MutableStateFlow(MainScreenViewState())

    init {
        observePreferences()
    }

    private fun observePreferences() {
        getPreferencesFlow()
            .filterNotNull()
            .onEach { preferences ->
                viewState.update {
                    it.copy(
                        uiMode = if (preferences.isDarkMode) UiMode.Dark else UiMode.Light
                    )
                }
            }.launchIn(viewModelScope)
    }

    override suspend fun hasStoredPreferences(): Boolean {
        return getPreferencesFlow().firstOrNull() != null
    }

    override fun updateSettings(
        isDarkMode: Boolean?,
        appLanguageCountryCode: String?
    ) {
        viewModelScope.launch {
            savePreferences(
                isDarkMode = isDarkMode ?: getPreferencesFlow().firstOrNull()?.isDarkMode ?: false,
                appLanguages = SupportedLanguages.getOrNull(appLanguageCountryCode) ?: getCurrentSystemLanguageOrDefault()
            )
        }
    }

    override fun toggleDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            updateSettings(isDarkMode = isDarkMode)
        }
    }
}
