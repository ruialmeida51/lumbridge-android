package com.eyther.lumbridge.domain.repository.currencyexchange

import com.eyther.lumbridge.domain.model.currencyexchange.CurrencyRates

interface CurrencyExchangeRepository {
    suspend fun getCurrencyRates(baseCurrency: String, toCurrency: String): CurrencyRates
}
