package com.eyther.lumbridge.data.datasource.user.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.eyther.lumbridge.data.datasource.user.local.UserProfileLocalDataSource.PreferencesKeys.COUNTRY_CODE
import com.eyther.lumbridge.data.datasource.user.local.UserProfileLocalDataSource.PreferencesKeys.EMAIL
import com.eyther.lumbridge.data.datasource.user.local.UserProfileLocalDataSource.PreferencesKeys.NAME
import com.eyther.lumbridge.data.di.LocalDataModule.UserProfileDataSource
import com.eyther.lumbridge.data.model.user.UserProfileCached
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class UserProfileLocalDataSource @Inject constructor(
    @UserProfileDataSource private val userProfileDataSource: DataStore<Preferences>
) {
    private object PreferencesKeys {
        val NAME = stringPreferencesKey("name")
        val EMAIL = stringPreferencesKey("email")
        val COUNTRY_CODE = stringPreferencesKey("country_code")
    }

    val userProfileFlow: Flow<UserProfileCached?> = userProfileDataSource.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val name = preferences[NAME] ?: return@map null
            val email = preferences[EMAIL] ?: return@map null
            val countryCode = preferences[COUNTRY_CODE] ?: return@map null

            UserProfileCached(
                name = name,
                email = email,
                countryCode = countryCode
            )
        }

    suspend fun saveUserData(userProfileCached: UserProfileCached) {
        userProfileDataSource.edit { preferences ->
            preferences[NAME] = userProfileCached.name
            preferences[EMAIL] = userProfileCached.email
            preferences[COUNTRY_CODE] = userProfileCached.countryCode
        }
    }
}