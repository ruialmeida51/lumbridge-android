package com.eyther.lumbridge.domain.model.currencyexchange

import java.util.Currency

enum class SupportedCurrencies(
    val currency: Currency
) {
    EUR(Currency.getInstance("EUR")),
    USD(Currency.getInstance("USD")),
    GBP(Currency.getInstance("GBP")),
    JPY(Currency.getInstance("JPY")),
    AUD(Currency.getInstance("AUD")),
    CAD(Currency.getInstance("CAD")),
    CHF(Currency.getInstance("CHF")),
    CNY(Currency.getInstance("CNY")),
    SEK(Currency.getInstance("SEK")),
    NZD(Currency.getInstance("NZD")),
    MXN(Currency.getInstance("MXN")),
    SGD(Currency.getInstance("SGD")),
    HKD(Currency.getInstance("HKD")),
    NOK(Currency.getInstance("NOK")),
    KRW(Currency.getInstance("KRW")),
    TRY(Currency.getInstance("TRY")),
    RUB(Currency.getInstance("RUB")),
    INR(Currency.getInstance("INR")),
    BRL(Currency.getInstance("BRL")),
    ZAR(Currency.getInstance("ZAR")),
    DKK(Currency.getInstance("DKK")),
    PLN(Currency.getInstance("PLN")),
    IDR(Currency.getInstance("IDR")),
    HUF(Currency.getInstance("HUF")),
    CZK(Currency.getInstance("CZK")),
    ILS(Currency.getInstance("ILS")),
    CLP(Currency.getInstance("CLP")),
    PHP(Currency.getInstance("PHP")),
    AED(Currency.getInstance("AED"));

    fun getHumanReadableName(): String =
        "${this.currency.displayName} (${this.currency.currencyCode})"

    fun getCurrencySymbol(): String = this.currency.symbol

    companion object {
        fun get(currencyCode: String) =
            SupportedCurrencies.entries.first { it.currency.currencyCode == currencyCode }
    }
}
