package com.eyther.lumbridge.features.profile.settings.model

import com.eyther.lumbridge.domain.model.locale.SupportedLanguages

data class ProfileAppSettingsScreenInputState(
    val isDarkMode: Boolean = false,
    val appLanguage: SupportedLanguages = SupportedLanguages.ENGLISH
)
