package com.eyther.lumbridge.data.model.appSettings

data class AppSettings(
    val isDarkMode: Boolean,
    val appLanguageCountryCode: String,
    val completedMortgageMigration: Boolean = false,
    val promptedAllowNotifications: Boolean = false,
)
