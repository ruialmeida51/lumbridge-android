package com.eyther.lumbridge.data.repository.currencyexchange

import com.eyther.lumbridge.data.datasource.currencyexchange.remote.CurrencyExchangeRemoteDataSource
import com.eyther.lumbridge.data.mapper.currencyexchange.toDomain
import com.eyther.lumbridge.domain.model.currencyexchange.CurrencyRates
import com.eyther.lumbridge.domain.repository.currencyexchange.CurrencyExchangeRepository
import com.eyther.lumbridge.shared.di.model.Schedulers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrencyExchangeRepositoryImpl @Inject constructor(
    private val currencyExchangeRemoteDataSource: CurrencyExchangeRemoteDataSource,
    private val schedulers: Schedulers
) : CurrencyExchangeRepository {

    companion object {
        const val MAX_REQUEST_AGE_IN_DAYS = 1L
    }

    override suspend fun getCurrencyRates(
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
