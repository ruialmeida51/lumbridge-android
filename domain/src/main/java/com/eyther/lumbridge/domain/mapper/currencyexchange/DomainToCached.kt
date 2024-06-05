package com.eyther.lumbridge.domain.mapper.currencyexchange

import com.eyther.lumbridge.data.model.currencyexchange.local.CurrencyRatesCached
import com.eyther.lumbridge.domain.model.currencyexchange.CurrencyRates

fun CurrencyRates.toCached() = CurrencyRatesCached(
    baseCurrency = baseCurrency,
    rates = rates.mapKeys { it.key.currencyCode },
    requestedAtInMillis = System.currentTimeMillis()
)
