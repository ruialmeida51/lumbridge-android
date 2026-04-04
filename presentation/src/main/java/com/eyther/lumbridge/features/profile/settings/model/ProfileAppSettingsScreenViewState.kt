package com.eyther.lumbridge.features.profile.settings.model

import com.eyther.lumbridge.domain.model.locale.SupportedLanguages

sealed interface ProfileAppSettingsScreenViewState {
    data object Loading: ProfileAppSettingsScreenViewState

    data class Content(
        val inputState: ProfileAppSettingsScreenInputState,
        val availableLanguages: List<SupportedLanguages>
    ): ProfileAppSettingsScreenViewState
}
