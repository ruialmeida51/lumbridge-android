package com.eyther.lumbridge.features.profile.settings.model

sealed interface ProfileAppSettingsScreenViewEffect {
    data class UpdateAppSettings(
        val isDarkMode: Boolean,
        val appLanguageCountryCode: String
    ) : ProfileAppSettingsScreenViewEffect
}
