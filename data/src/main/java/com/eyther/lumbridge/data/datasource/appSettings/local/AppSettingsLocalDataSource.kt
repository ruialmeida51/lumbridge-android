package com.eyther.lumbridge.data.datasource.appSettings.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.eyther.lumbridge.data.datasource.appSettings.local.AppSettingsLocalDataSource.PreferencesKeys.APP_LANGUAGE_COUNTRY_CODE
import com.eyther.lumbridge.data.datasource.appSettings.local.AppSettingsLocalDataSource.PreferencesKeys.IS_DARK_MODE
import com.eyther.lumbridge.data.di.LocalDataModule.AppSettingsDataStore
import com.eyther.lumbridge.data.model.appSettings.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class AppSettingsLocalDataSource @Inject constructor(
    @AppSettingsDataStore private val appSettingsDataStore: DataStore<Preferences>
) {
    private object PreferencesKeys {
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val APP_LANGUAGE_COUNTRY_CODE = stringPreferencesKey("app_language_country_code")
    }

    val appSettingsFlow: Flow<AppSettings?> = appSettingsDataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val isDarkMode = preferences[IS_DARK_MODE] ?: return@map null
            val appLanguageCountryCode = preferences[APP_LANGUAGE_COUNTRY_CODE] ?: return@map null

            AppSettings(
                isDarkMode = isDarkMode,
                appLanguageCountryCode = appLanguageCountryCode
            )
        }

    suspend fun saveAppSettings(appSettings: AppSettings) {
        appSettingsDataStore.edit { preferences ->
            preferences[IS_DARK_MODE] = appSettings.isDarkMode
            preferences[APP_LANGUAGE_COUNTRY_CODE] = appSettings.appLanguageCountryCode
        }
    }
}
