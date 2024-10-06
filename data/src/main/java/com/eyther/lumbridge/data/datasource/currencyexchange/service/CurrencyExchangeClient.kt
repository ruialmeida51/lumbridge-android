package com.eyther.lumbridge.data.datasource.currencyexchange.service

import com.eyther.lumbridge.data.model.currencyexchange.remote.CurrencyRatesRemote
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CurrencyExchangeClient {

    /**
     * Get exchange rates for a base currency to a target currency.
     *
     * @param baseCurrency The base currency to convert from, e.g. "USD", in ISO 4217 format.
     * @param toCurrency The target currency to convert to, e.g. "EUR", in ISO 4217 format.
     */
    @GET("{baseCurrency}")
    suspend fun getExchangeRates(
        @Path("baseCurrency") baseCurrency: String,
        @Query("target") toCurrency: String
    ): Response<CurrencyRatesRemote>
}
