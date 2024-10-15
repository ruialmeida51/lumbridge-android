package com.eyther.lumbridge.domain.repository.currencyexchange

import com.eyther.lumbridge.data.datasource.currencyexchange.remote.CurrencyExchangeRemoteDataSource
import com.eyther.lumbridge.shared.di.model.Schedulers
import com.eyther.lumbridge.domain.mapper.currencyexchange.toDomain
import com.eyther.lumbridge.domain.model.currencyexchange.CurrencyRates
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrencyExchangeRepository @Inject constructor(
    private val currencyExchangeRemoteDataSource: CurrencyExchangeRemoteDataSource,
    private val schedulers: Schedulers
) {
    companion object {
        // The database is updated every 24 hours and we have a limit of requests per hour/day,
        // since it's a free API. We can't make requests every time we need the currency rates.
        const val MAX_REQUEST_AGE_IN_DAYS = 1L
    }

    /**
     * Gets the currency rates from an external API. If the currency rates are already cached,
     * it will return the cached currency rates, as we have limited API calls.
     *
     * @param baseCurrency the base currency to get the currency rates from, in ISO 4217 format
     * @param toCurrency the target currency to get the currency rates to, in ISO 4217 format
     *
     * @return the currency rates
     */
    suspend fun getCurrencyRates(
        baseCurrency: String,
        toCurrency: String
    ): CurrencyRates {
        val remoteCurrencyRates = withContext(schedulers.io) {
            currencyExchangeRemoteDataSource.getCurrencyRates(baseCurrency, toCurrency)
        }

        return withContext(schedulers.cpu) {
            requireNotNull(remoteCurrencyRates?.toDomain())
        }
    }
}
