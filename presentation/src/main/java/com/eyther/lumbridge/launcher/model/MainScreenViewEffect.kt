package com.eyther.lumbridge.launcher.model

sealed interface MainScreenViewEffect {
    data class UpdateAppLanguage(
        val countryCode: String
    ) : MainScreenViewEffect
}
