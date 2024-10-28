package com.eyther.lumbridge.data.datasource.user.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.eyther.lumbridge.data.di.LocalDataModule.UserMortgageDataStore
import com.eyther.lumbridge.data.model.user.local.UserMortgageCached
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@Deprecated("This class is deprecated and will be removed in the future, please use [LoanLocalDataSource] instead as we're migrating this" +
    " information to a Room Database.")
class UserMortgageLocalDataSource @Inject constructor(
    @UserMortgageDataStore private val userMortgageDataStore: DataStore<Preferences>
) {
    private object PreferencesKeys {
        val EURIBOR = floatPreferencesKey("euribor")
        val SPREAD = floatPreferencesKey("spread")
        val FIXED_INTEREST_RATE = floatPreferencesKey("fixed_interest_rate")
        val LOAN_AMOUNT = floatPreferencesKey("loan_amount")
        val STARTING_DATE = stringPreferencesKey("starting_date")
        val END_DATE = stringPreferencesKey("end_date")
    }

    val userMortgageFlow: Flow<UserMortgageCached?> = userMortgageDataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val loanAmount = preferences[PreferencesKeys.LOAN_AMOUNT] ?: return@map null
            val startDate = preferences[PreferencesKeys.STARTING_DATE] ?: return@map null
            val endDate = preferences[PreferencesKeys.END_DATE] ?: return@map null
            val euribor = preferences[PreferencesKeys.EURIBOR]
            val spread = preferences[PreferencesKeys.SPREAD]
            val fixedInterestRate = preferences[PreferencesKeys.FIXED_INTEREST_RATE]

            UserMortgageCached(
                euribor = euribor,
                spread = spread,
                loanAmount = loanAmount,
                fixedInterestRate = fixedInterestRate,
                startDate = startDate,
                endDate = endDate
            )
        }
}
