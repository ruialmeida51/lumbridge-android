package com.eyther.lumbridge.domain.repository.currencyexchange

import com.eyther.lumbridge.domain.model.currencyexchange.CurrencyRates

interface CurrencyExchangeRepository {
    companion object {
        const val MAX_REQUEST_AGE_IN_DAYS = 1L
    }
    suspend fun getCurrencyRates(baseCurrency: String, toCurrency: String): CurrencyRates
}
