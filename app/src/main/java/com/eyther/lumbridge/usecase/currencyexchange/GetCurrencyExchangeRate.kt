package com.eyther.lumbridge.usecase.currencyexchange

import com.eyther.lumbridge.domain.repository.currencyexchange.CurrencyExchangeRepository
import java.util.Currency
import javax.inject.Inject

class GetCurrencyExchangeRate @Inject constructor(
    private val currencyExchangeRepository: CurrencyExchangeRepository
) {
    /**
     * Gets the currency exchange rate.
     *
     * @param fromCurrency the from currency to get the exchange rate from
     * @param targetCurrency the target currency to get the exchange rate to
     *
     * @return the exchange rate
     */
    suspend fun getExchangeRate(
        fromCurrency: Currency,
        targetCurrency: Currency
    ): Float {
        val currencyRates = currencyExchangeRepository.getCurrencyRates(
            baseCurrency = fromCurrency.currencyCode,
            toCurrency = targetCurrency.currencyCode
        )

        return currencyRates.rate.toFloat()
    }
}
