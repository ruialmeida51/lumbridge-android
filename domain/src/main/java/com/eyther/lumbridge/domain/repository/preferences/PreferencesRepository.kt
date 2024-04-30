package com.eyther.lumbridge.domain.repository.preferences

import com.eyther.lumbridge.data.datasource.appSettings.local.AppSettingsLocalDataSource
import com.eyther.lumbridge.domain.mapper.preferences.toCached
import com.eyther.lumbridge.domain.mapper.preferences.toDomain
import com.eyther.lumbridge.domain.model.preferences.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PreferencesRepository @Inject constructor(
    private val appSettingsLocalDataSource: AppSettingsLocalDataSource
) {
    private suspend fun getPreferences(): Preferences? {
        return appSettingsLocalDataSource.appSettingsFlow.firstOrNull()?.toDomain()
    }

    suspend fun setDarkMode(isDarkMode: Boolean) = withContext(Dispatchers.IO) {
        val newPreferences = getPreferences()?.copy(isDarkMode = isDarkMode)
            ?: Preferences(isDarkMode = isDarkMode)

        appSettingsLocalDataSource.saveAppSettings(appSettings = newPreferences.toCached())
    }

    suspend fun isDarkMode(): Boolean? = withContext(Dispatchers.IO) {
        getPreferences()?.isDarkMode
    }
}
