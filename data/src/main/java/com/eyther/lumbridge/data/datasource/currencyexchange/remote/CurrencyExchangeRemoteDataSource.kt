package com.eyther.lumbridge.data.datasource.currencyexchange.remote

import com.eyther.lumbridge.data.datasource.currencyexchange.service.CurrencyExchangeClient
import com.eyther.lumbridge.data.model.currencyexchange.remote.CurrencyRatesRemote
import javax.inject.Inject

class CurrencyExchangeRemoteDataSource @Inject constructor(
    private val currencyExchangeClient: CurrencyExchangeClient
) {
    suspend fun getCurrencyRates(baseCurrency: String): CurrencyRatesRemote? {
        return if (currencyExchangeClient.getExchangeRates(baseCurrency).isSuccessful) {
            currencyExchangeClient.getExchangeRates(baseCurrency).body()
        } else {
            null
        }
    }
}
