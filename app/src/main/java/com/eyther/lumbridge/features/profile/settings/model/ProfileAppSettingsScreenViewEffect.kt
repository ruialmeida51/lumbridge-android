package com.eyther.lumbridge.features.profile.settings.model

sealed interface ProfileAppSettingsScreenViewEffect {
    data class ChangeAppLanguage(
        val appLanguageCountryCode: String
    ) : ProfileAppSettingsScreenViewEffect
}
