package com.eyther.lumbridge.features.profile.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.profile.settings.model.ProfileAppSettingsScreenViewState
import com.eyther.lumbridge.usecase.preferences.GetPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProfileAppAppSettingsScreenViewModel @Inject constructor(
    private val getPreferences: GetPreferences
) : ViewModel(), IProfileAppSettingsScreenViewModel {

    override val viewState = MutableStateFlow<ProfileAppSettingsScreenViewState>(
        ProfileAppSettingsScreenViewState.Loading
    )

    init {
        observeChanges()
    }

    private fun observeChanges() {
        getPreferences()
            .onEach { preferences ->
                viewState.update {
                    ProfileAppSettingsScreenViewState.Content(
                        isDarkModeEnabled = preferences?.isDarkMode
                    )
                }
            }.launchIn(viewModelScope)
    }
}
