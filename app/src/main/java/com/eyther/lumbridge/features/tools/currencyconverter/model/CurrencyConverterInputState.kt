package com.eyther.lumbridge.features.tools.currencyconverter.model

import com.eyther.lumbridge.domain.model.currencyexchange.SupportedCurrencies
import com.eyther.lumbridge.ui.common.composables.model.TextInputState

data class CurrencyConverterInputState(
    val fromCurrency: SupportedCurrencies = SupportedCurrencies.EUR,
    val toCurrency: SupportedCurrencies = SupportedCurrencies.USD,
    val fromAmount: TextInputState = TextInputState()
)
