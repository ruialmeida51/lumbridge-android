package com.eyther.lumbridge.domain.repository.currencyexchange

import com.eyther.lumbridge.shared.di.model.Schedulers
import com.eyther.lumbridge.domain.mapper.currencyexchange.toDomain
import com.eyther.lumbridge.domain.model.currencyexchange.CurrencyRates
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface CurrencyExchangeRepository {
    val remoteCurrencyRates
    suspend fun getCurrencyRates( baseCurrency: String, toCurrency: String ): CurrencyRates
}
