package com.eyther.lumbridge.data.datasource.currencyexchange.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.eyther.lumbridge.data.di.LocalDataModule.CurrencyRatesDataStore
import com.eyther.lumbridge.data.di.UtilModule
import com.eyther.lumbridge.data.di.UtilModule.DefaultGson
import com.eyther.lumbridge.data.model.currencyexchange.local.CurrencyRatesCached
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.lang.reflect.Type
import javax.inject.Inject

class CurrencyExchangeLocalDataSource @Inject constructor(
    @CurrencyRatesDataStore private val currencyRatesDataStore: DataStore<Preferences>,
    @DefaultGson private val gson: Gson
) {
    private object PreferencesKeys {
        val BASE_CURRENCY = stringPreferencesKey("base_currency")
        val rates = stringPreferencesKey("rates")
        val timestamp = longPreferencesKey("timestamp")
    }

    val currencyRatesFlow: Flow<CurrencyRatesCached?> = currencyRatesDataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val baseCurrency = preferences[PreferencesKeys.BASE_CURRENCY] ?: return@map null
            val rates = preferences[PreferencesKeys.rates] ?: return@map null
            val timestamp = preferences[PreferencesKeys.timestamp] ?: return@map null

            val jsonType: Type = object : TypeToken<Map<String?, Double?>?>() {}.type

            CurrencyRatesCached(
                baseCurrency = baseCurrency,
                rates = gson.fromJson(rates, jsonType),
                requestedAtInMillis = timestamp
            )
        }

    suspend fun saveCurrencyRates(currencyRatesCached: CurrencyRatesCached) {
        currencyRatesDataStore.edit { preferences ->
            preferences[PreferencesKeys.BASE_CURRENCY] = currencyRatesCached.baseCurrency
            preferences[PreferencesKeys.rates] = gson.toJson(currencyRatesCached.rates)
            preferences[PreferencesKeys.timestamp] = currencyRatesCached.requestedAtInMillis
        }
    }
}
