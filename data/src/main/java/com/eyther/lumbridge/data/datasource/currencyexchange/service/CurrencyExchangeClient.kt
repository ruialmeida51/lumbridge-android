package com.eyther.lumbridge.data.datasource.currencyexchange.service

import com.eyther.lumbridge.data.model.currencyexchange.remote.CurrencyRatesRemote
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyExchangeClient {
    @GET("{baseCurrency}")
    suspend fun getExchangeRates(
        @Path("baseCurrency") baseCurrency: String
    ): Response<CurrencyRatesRemote>
}
