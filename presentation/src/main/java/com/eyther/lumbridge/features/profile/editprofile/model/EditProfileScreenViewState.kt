package com.eyther.lumbridge.features.profile.editprofile.model

import com.eyther.lumbridge.domain.model.locale.SupportedLocales

sealed interface EditProfileScreenViewState {
    data object Loading : EditProfileScreenViewState

    data class Content(
        val availableLocales: List<SupportedLocales>,
        val inputState: EditProfileScreenInputState,
        val shouldEnableSaveButton: Boolean = false
    ): EditProfileScreenViewState
}
