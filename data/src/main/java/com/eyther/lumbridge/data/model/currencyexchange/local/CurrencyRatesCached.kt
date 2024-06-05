package com.eyther.lumbridge.data.model.currencyexchange.local

data class CurrencyRatesCached(
    val baseCurrency: String,
    val rates: Map<String, Double>,
    val requestedAtInMillis: Long
)
