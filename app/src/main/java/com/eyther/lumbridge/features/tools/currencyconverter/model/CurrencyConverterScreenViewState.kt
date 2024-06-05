package com.eyther.lumbridge.features.tools.currencyconverter.model

import com.eyther.lumbridge.domain.model.currencyexchange.SupportedCurrencies

sealed interface CurrencyConverterScreenViewState {
    data object Loading : CurrencyConverterScreenViewState

    data class Content(
        val inputState: CurrencyConverterInputState = CurrencyConverterInputState(),
        val availableCurrencies: List<SupportedCurrencies>,
        val shouldEnableCalculateButton: Boolean = false,
        val isCalculating: Boolean = false,
        val exchangeRate: Float? = null,
        val toExchangedAmount: Float? = null,
        val hasError: Boolean = false
    ) : CurrencyConverterScreenViewState {

        fun hasExchangeRate(): Boolean = exchangeRate != null && toExchangedAmount != null
        fun displayCalculation() = hasExchangeRate() || hasError
    }
}