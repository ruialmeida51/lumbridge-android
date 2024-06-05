package com.eyther.lumbridge.domain.repository.currencyexchange

import com.eyther.lumbridge.data.datasource.currencyexchange.local.CurrencyExchangeLocalDataSource
import com.eyther.lumbridge.data.datasource.currencyexchange.remote.CurrencyExchangeRemoteDataSource
import com.eyther.lumbridge.domain.mapper.currencyexchange.toCached
import com.eyther.lumbridge.domain.mapper.currencyexchange.toDomain
import com.eyther.lumbridge.domain.model.currencyexchange.CurrencyRates
import com.eyther.lumbridge.domain.time.toLocalDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrencyExchangeRepository @Inject constructor(
    private val currencyExchangeRemoteDataSource: CurrencyExchangeRemoteDataSource,
    private val currencyExchangeLocalDataSource: CurrencyExchangeLocalDataSource
) {
    companion object {
       // The database is updated every 24 hours and we have a limit of requests per hour/day,
        // since it's a free API. We can't make requests every time we need the currency rates.
        private const val MAX_REQUEST_AGE_IN_DAYS = 1L
    }

    /**
     * Gets the currency rates from an external API. If the currency rates are already cached,
     * it will return the cached currency rates, as we have limited API calls.
     *
     * @param baseCurrency the base currency to get the currency rates from
     *
     * @return the currency rates
     */
    suspend fun getCurrencyRates(baseCurrency: String): CurrencyRates =
        withContext(Dispatchers.Default) {
            val localCurrencyRates = withContext(Dispatchers.IO) {
                currencyExchangeLocalDataSource.currencyRatesFlow.firstOrNull()
            }

            if (localCurrencyRates != null) {
                val requestedAt = localCurrencyRates.requestedAtInMillis
                val now = System.currentTimeMillis()

                val requestAge = (requestedAt + now).toLocalDateTime()
                val maxRequestAge = requestAge.plusDays(MAX_REQUEST_AGE_IN_DAYS)

                if (
                    localCurrencyRates.baseCurrency == baseCurrency &&
                    requestAge.isBefore(maxRequestAge)
                ) {
                    return@withContext localCurrencyRates.toDomain()
                }
            }

            val remoteCurrencyRates = withContext(Dispatchers.IO) {
                currencyExchangeRemoteDataSource.getCurrencyRates(baseCurrency)
            }

            return@withContext requireNotNull(remoteCurrencyRates?.toDomain())
                .also { currencyExchangeLocalDataSource.saveCurrencyRates(it.toCached()) }
        }
}