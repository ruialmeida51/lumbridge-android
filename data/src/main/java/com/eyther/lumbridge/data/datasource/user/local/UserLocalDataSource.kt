package com.eyther.lumbridge.data.datasource.user.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.eyther.lumbridge.data.datasource.user.local.UserLocalDataSource.PreferencesKeys.COUNTRY_CODE
import com.eyther.lumbridge.data.datasource.user.local.UserLocalDataSource.PreferencesKeys.GROSS_SALARY
import com.eyther.lumbridge.data.di.DataModule.UserDataStore
import com.eyther.lumbridge.data.model.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
    @UserDataStore private val userDataStore: DataStore<Preferences>
) {
    private object PreferencesKeys {
        val GROSS_SALARY = floatPreferencesKey("gross_salary")
        val COUNTRY_CODE = stringPreferencesKey("country_code")
    }

    val userPreferencesFlow: Flow<UserEntity?> = userDataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val grossSalary = preferences[GROSS_SALARY] ?: return@map null
            val countryCode = preferences[COUNTRY_CODE] ?: return@map null

            UserEntity(
                countryCode = countryCode,
                grossSalary = grossSalary
            )
        }

    suspend fun saveUserData(userEntity: UserEntity) {
        userDataStore.edit { preferences ->
            preferences[GROSS_SALARY] = userEntity.grossSalary
            preferences[COUNTRY_CODE] = userEntity.countryCode
        }
    }
}