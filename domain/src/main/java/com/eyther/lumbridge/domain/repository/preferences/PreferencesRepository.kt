package com.eyther.lumbridge.domain.repository.preferences

import com.eyther.lumbridge.data.datasource.appSettings.local.AppSettingsLocalDataSource
import com.eyther.lumbridge.domain.mapper.preferences.toCached
import com.eyther.lumbridge.domain.mapper.preferences.toDomain
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

    // Private

    // Suspend
    private suspend fun getPreferences(): Preferences? {
        return appSettingsLocalDataSource.appSettingsFlow.firstOrNull()?.toDomain()
    }

    // Flow
    fun getPreferencesFlow(): Flow<Preferences?> {
        return appSettingsLocalDataSource.appSettingsFlow.map { it?.toDomain() }
    }

    // Public

    // Suspend
    suspend fun setDarkMode(isDarkMode: Boolean) = withContext(Dispatchers.IO) {
        val newPreferences = getPreferences()?.copy(isDarkMode = isDarkMode)
            ?: Preferences(isDarkMode = isDarkMode)

        appSettingsLocalDataSource.saveAppSettings(appSettings = newPreferences.toCached())
    }
}
