package com.eyther.lumbridge.features.profile.editprofile.model

sealed interface EditProfileScreenViewEffects {
    data object None: EditProfileScreenViewEffects

    data class ShowError(val message: String) : EditProfileScreenViewEffects
}
