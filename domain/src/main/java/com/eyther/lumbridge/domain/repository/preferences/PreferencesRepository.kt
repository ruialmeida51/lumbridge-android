package com.eyther.lumbridge.domain.repository.preferences

import com.eyther.lumbridge.data.datasource.appSettings.local.AppSettingsLocalDataSource
import com.eyther.lumbridge.domain.mapper.preferences.toCached
import com.eyther.lumbridge.domain.mapper.preferences.toDomain
import com.eyther.lumbridge.domain.model.locale.SupportedLanguages
import com.eyther.lumbridge.domain.model.preferences.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PreferencesRepository @Inject constructor(
    private val appSettingsLocalDataSource: AppSettingsLocalDataSource
) {

    private suspend fun getPreferences(): Preferences? {
        return appSettingsLocalDataSource.appSettingsFlow.firstOrNull()?.toDomain()
    }

    fun getPreferencesFlow(): Flow<Preferences?> {
        return appSettingsLocalDataSource.appSettingsFlow.map { it?.toDomain() }
    }

    /**
     * Update the preferences. Tries to get the current preferences and updates the values, if these do not exist
     * it creates a new one with the given values.
     *
     * @param isDarkMode The new value for the dark mode preference.
     * @param appLanguage The new value for the app language preference.
     */
    suspend fun updatePreferences(
        isDarkMode: Boolean,
        appLanguage: SupportedLanguages
    ) = withContext(Dispatchers.IO) {
        val currentPreferences = getPreferences()

        val newPreferences = currentPreferences?.copy(
            isDarkMode = isDarkMode,
            appLanguage = appLanguage
        ) ?: Preferences(
            isDarkMode = isDarkMode,
            appLanguage = appLanguage
        )

        appSettingsLocalDataSource.saveAppSettings(appSettings = newPreferences.toCached())
    }
}
