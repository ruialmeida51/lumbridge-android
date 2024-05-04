package com.eyther.lumbridge.data.datasource.user.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import com.eyther.lumbridge.data.datasource.user.local.UserFinancialsLocalDataSource.PreferencesKeys.ANNUAL_GROSS_SALARY
import com.eyther.lumbridge.data.datasource.user.local.UserFinancialsLocalDataSource.PreferencesKeys.FOOD_CARD_PER_DIEM
import com.eyther.lumbridge.data.datasource.user.local.UserFinancialsLocalDataSource.PreferencesKeys.HANDICAPPED
import com.eyther.lumbridge.data.datasource.user.local.UserFinancialsLocalDataSource.PreferencesKeys.IRS_WITH_PARTNER
import com.eyther.lumbridge.data.datasource.user.local.UserFinancialsLocalDataSource.PreferencesKeys.LUXURIES_PERCENTAGE
import com.eyther.lumbridge.data.datasource.user.local.UserFinancialsLocalDataSource.PreferencesKeys.MARRIED
import com.eyther.lumbridge.data.datasource.user.local.UserFinancialsLocalDataSource.PreferencesKeys.NECESSITIES_PERCENTAGE
import com.eyther.lumbridge.data.datasource.user.local.UserFinancialsLocalDataSource.PreferencesKeys.NUMBER_OF_DEPENDANTS
import com.eyther.lumbridge.data.datasource.user.local.UserFinancialsLocalDataSource.PreferencesKeys.SAVINGS_PERCENTAGE
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
        val SAVINGS_PERCENTAGE = floatPreferencesKey("savings_percentage")
        val NECESSITIES_PERCENTAGE = floatPreferencesKey("necessities_percentage")
        val LUXURIES_PERCENTAGE = floatPreferencesKey("luxuries_percentage")
        val NUMBER_OF_DEPENDANTS = intPreferencesKey("number_of_dependants")
        val IRS_WITH_PARTNER = booleanPreferencesKey("irs_with_partner")
        val MARRIED = booleanPreferencesKey("married")
        val HANDICAPPED = booleanPreferencesKey("handicapped")
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
            val savingsPercentage = preferences[SAVINGS_PERCENTAGE]
            val necessitiesPercentage = preferences[NECESSITIES_PERCENTAGE]
            val luxuriesPercentage = preferences[LUXURIES_PERCENTAGE]
            val numberOfDependants = preferences[NUMBER_OF_DEPENDANTS]
            val irsWithPartner = preferences[IRS_WITH_PARTNER] ?: false
            val married = preferences[MARRIED] ?: false
            val handicapped = preferences[HANDICAPPED] ?: false

            UserFinancialsCached(
                annualGrossSalary = annualGrossSalary,
                foodCardPerDiem = foodCardPerDiem,
                savingsPercentage = savingsPercentage?.toInt(),
                necessitiesPercentage = necessitiesPercentage?.toInt(),
                luxuriesPercentage = luxuriesPercentage?.toInt(),
                numberOfDependants = numberOfDependants,
                irsWithPartner = irsWithPartner,
                married = married,
                handicapped = handicapped
            )
        }

    suspend fun saveUserFinancials(userProfileCached: UserFinancialsCached) {
        userFinancialsDataStore.edit { preferences ->
            preferences[ANNUAL_GROSS_SALARY] = userProfileCached.annualGrossSalary
            preferences[FOOD_CARD_PER_DIEM] = userProfileCached.foodCardPerDiem
            preferences[IRS_WITH_PARTNER] = userProfileCached.irsWithPartner
            preferences[MARRIED] = userProfileCached.married
            preferences[HANDICAPPED] = userProfileCached.handicapped

            userProfileCached.savingsPercentage?.toFloat()?.let {
                preferences[SAVINGS_PERCENTAGE] = it
            } ?: preferences.remove(SAVINGS_PERCENTAGE)


            userProfileCached.necessitiesPercentage?.toFloat()?.let {
                preferences[NECESSITIES_PERCENTAGE] = it
            } ?: preferences.remove(NECESSITIES_PERCENTAGE)

            userProfileCached.luxuriesPercentage?.toFloat()?.let {
                preferences[LUXURIES_PERCENTAGE] = it
            } ?: preferences.remove(LUXURIES_PERCENTAGE)

            userProfileCached.numberOfDependants?.let {
                preferences[NUMBER_OF_DEPENDANTS] = it
            } ?: preferences.remove(NUMBER_OF_DEPENDANTS)
        }
    }
}
