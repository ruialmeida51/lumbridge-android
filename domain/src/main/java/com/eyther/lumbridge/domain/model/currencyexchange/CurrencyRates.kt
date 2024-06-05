package com.eyther.lumbridge.domain.model.currencyexchange

import java.util.Currency

data class CurrencyRates(
    val baseCurrency: String,
    val rates: Map<Currency, Double>,
    val requestedAtInMillis: Long
)
