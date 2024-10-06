package com.eyther.lumbridge.data.datasource.currencyexchange.remote

import com.eyther.lumbridge.data.datasource.currencyexchange.service.CurrencyExchangeClient
import com.eyther.lumbridge.data.model.currencyexchange.remote.CurrencyRatesRemote
import javax.inject.Inject

class CurrencyExchangeRemoteDataSource @Inject constructor(
    private val currencyExchangeClient: CurrencyExchangeClient
) {
    /**
     * Get exchange rates for a base currency to a target currency.
     *
     * Note, both currencies should be in ISO 4217 format.
     */
    suspend fun getCurrencyRates(
        baseCurrency: String,
        toCurrency: String
    ): CurrencyRatesRemote? {
        val request = currencyExchangeClient.getExchangeRates(baseCurrency, toCurrency)

        return if (request.isSuccessful) {
            request.body()
        } else {
            null
        }
    }
}
