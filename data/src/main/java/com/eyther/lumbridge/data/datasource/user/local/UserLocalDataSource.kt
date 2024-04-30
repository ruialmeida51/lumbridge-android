package com.eyther.lumbridge.data.datasource.user.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.eyther.lumbridge.data.datasource.user.local.UserLocalDataSource.PreferencesKeys.COUNTRY_CODE
import com.eyther.lumbridge.data.datasource.user.local.UserLocalDataSource.PreferencesKeys.ANNUAL_GROSS_SALARY
import com.eyther.lumbridge.data.datasource.user.local.UserLocalDataSource.PreferencesKeys.FOOD_CARD_PER_DIEM
import com.eyther.lumbridge.data.di.DataModule.UserDataStore
import com.eyther.lumbridge.data.model.user.UserCached
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
    @UserDataStore private val userDataStore: DataStore<Preferences>
) {
    private object PreferencesKeys {
        val ANNUAL_GROSS_SALARY = floatPreferencesKey("annual_gross_salary")
        val FOOD_CARD_PER_DIEM = floatPreferencesKey("food_card_per_diem")
        val COUNTRY_CODE = stringPreferencesKey("country_code")
    }

    val userPreferencesFlow: Flow<UserCached?> = userDataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val annualGrossSalary = preferences[ANNUAL_GROSS_SALARY] ?: return@map null
            val foodCardPerDiem = preferences[FOOD_CARD_PER_DIEM] ?: return@map null
            val countryCode = preferences[COUNTRY_CODE] ?: return@map null

            UserCached(
                countryCode = countryCode,
                annualGrossSalary = annualGrossSalary,
                foodCardPerDiem = foodCardPerDiem
            )
        }

    suspend fun saveUserData(userCached: UserCached) {
        userDataStore.edit { preferences ->
            preferences[ANNUAL_GROSS_SALARY] = userCached.annualGrossSalary
            preferences[FOOD_CARD_PER_DIEM] = userCached.foodCardPerDiem
            preferences[COUNTRY_CODE] = userCached.countryCode
        }
    }
}