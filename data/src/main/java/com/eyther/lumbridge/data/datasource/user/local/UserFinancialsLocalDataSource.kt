package com.eyther.lumbridge.data.datasource.user.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import com.eyther.lumbridge.data.datasource.user.local.UserFinancialsLocalDataSource.PreferencesKeys.ANNUAL_GROSS_SALARY
import com.eyther.lumbridge.data.datasource.user.local.UserFinancialsLocalDataSource.PreferencesKeys.FOOD_CARD_PER_DIEM
import com.eyther.lumbridge.data.di.LocalDataModule.UserFinancialsDataStore
import com.eyther.lumbridge.data.model.user.UserFinancialsCached
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class UserFinancialsLocalDataSource @Inject constructor(
    @UserFinancialsDataStore private val userFinancialsDataStore: DataStore<Preferences>
) {
    private object PreferencesKeys {
        val ANNUAL_GROSS_SALARY = floatPreferencesKey("annual_gross_salary")
        val FOOD_CARD_PER_DIEM = floatPreferencesKey("food_card_per_diem")
    }

    val userFinancialsFlow: Flow<UserFinancialsCached?> = userFinancialsDataStore.data
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

            UserFinancialsCached(
                annualGrossSalary = annualGrossSalary,
                foodCardPerDiem = foodCardPerDiem
            )
        }

    suspend fun saveUserFinancials(userProfileCached: UserFinancialsCached) {
        userFinancialsDataStore.edit { preferences ->
            preferences[ANNUAL_GROSS_SALARY] = userProfileCached.annualGrossSalary
            preferences[FOOD_CARD_PER_DIEM] = userProfileCached.foodCardPerDiem
        }
    }
}
