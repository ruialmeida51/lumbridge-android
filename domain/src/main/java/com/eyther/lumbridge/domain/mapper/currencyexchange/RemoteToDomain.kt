package com.eyther.lumbridge.domain.mapper.currencyexchange

import com.eyther.lumbridge.data.model.currencyexchange.remote.CurrencyRatesRemote
import com.eyther.lumbridge.domain.model.currencyexchange.CurrencyRates

fun CurrencyRatesRemote.toDomain(): CurrencyRates {
    val data = checkNotNull(data) { "💥 currency data is null!" }

    return CurrencyRates(
        baseCurrency = checkNotNull(data.base),
        toCurrency = checkNotNull(data.target),
        rate = checkNotNull(data.rate),
        unit = checkNotNull(data.unit),
        requestedAtInMillis = System.currentTimeMillis()
    )
}
