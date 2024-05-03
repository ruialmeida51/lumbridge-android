package com.eyther.lumbridge.domain.model.locale

import java.util.Currency

// ISO 3166 ALPHA 2 for locale,
// ISO 4217 for currency
enum class SupportedLocales(
    val countryCode: String,
    private val currency: Currency
) {
    PORTUGAL("pt", Currency.getInstance("EUR"));

    fun getCurrencySymbol(): String = this.currency.symbol

    companion object {
        fun get(countryCode: String) =
            SupportedLocales.entries.first { it.countryCode == countryCode }
    }
}
