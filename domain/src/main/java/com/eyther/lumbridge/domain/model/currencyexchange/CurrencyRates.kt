package com.eyther.lumbridge.domain.model.currencyexchange

data class CurrencyRates(
    val baseCurrency: String,
    val toCurrency: String,
    val rate: Double,
    val unit: Int,
    val requestedAtInMillis: Long
)
