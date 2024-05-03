package com.eyther.lumbridge.features.profile.settings.model

sealed interface ProfileAppSettingsScreenViewState {
    data object Loading: ProfileAppSettingsScreenViewState

    data class Content(
        val isDarkModeEnabled: Boolean?
    ): ProfileAppSettingsScreenViewState
}
