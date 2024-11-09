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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class AppSettingsLocalDataSource @Inject constructor(
    @AppSettingsDataStore private val appSettingsDataStore: DataStore<Preferences>
) {
    private object PreferencesKeys {
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val APP_LANGUAGE_COUNTRY_CODE = stringPreferencesKey("app_language_country_code")
        val COMPLETED_MORTGAGE_MIGRATION = booleanPreferencesKey("completed_mortgage_migration")
        val COMPLETED_NET_SALARY_SNAPSHOT_MIGRATION = booleanPreferencesKey("completed_net_salary_snapshot_migration")
        val PROMPTED_ALLOW_NOTIFICATIONS = booleanPreferencesKey("prompted_allow_notifications")
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
            val completedMortgageMigration = preferences[PreferencesKeys.COMPLETED_MORTGAGE_MIGRATION] == true
            val completedNetSalarySnapshotMigration = preferences[PreferencesKeys.COMPLETED_NET_SALARY_SNAPSHOT_MIGRATION] == true
            val promptedAllowNotifications = preferences[PreferencesKeys.PROMPTED_ALLOW_NOTIFICATIONS] == true

            AppSettings(
                isDarkMode = isDarkMode,
                appLanguageCountryCode = appLanguageCountryCode,
                completedMortgageMigration = completedMortgageMigration,
                completedNetSalarySnapshotMigration = completedNetSalarySnapshotMigration,
                promptedAllowNotifications = promptedAllowNotifications
            )
        }

    suspend fun getCompletedMortgageMigration(): Boolean {
        return appSettingsFlow.map { it?.completedMortgageMigration == true }.first()
    }

    suspend fun getCompletedNetSalarySnapshotMigration(): Boolean {
        return appSettingsFlow.map { it?.completedNetSalarySnapshotMigration == true }.first()
    }

    suspend fun saveAppSettings(appSettings: AppSettings) {
        appSettingsDataStore.edit { preferences ->
            preferences[IS_DARK_MODE] = appSettings.isDarkMode
            preferences[APP_LANGUAGE_COUNTRY_CODE] = appSettings.appLanguageCountryCode
        }
    }

    suspend fun saveCompletedMortgageMigration(completed: Boolean) {
        appSettingsDataStore.edit { preferences ->
            preferences[PreferencesKeys.COMPLETED_MORTGAGE_MIGRATION] = completed
        }
    }

    suspend fun savePromptedAllowNotifications(prompted: Boolean) {
        appSettingsDataStore.edit { preferences ->
            preferences[PreferencesKeys.PROMPTED_ALLOW_NOTIFICATIONS] = prompted
        }
    }

    suspend fun saveCompletedNetSalarySnapshotMigration(completed: Boolean) {
        appSettingsDataStore.edit { preferences ->
            preferences[PreferencesKeys.COMPLETED_NET_SALARY_SNAPSHOT_MIGRATION] = completed
        }
    }
}
