package com.eyther.lumbridge.domain.mapper.currencyexchange

import com.eyther.lumbridge.data.model.currencyexchange.local.CurrencyRatesCached
import com.eyther.lumbridge.domain.model.currencyexchange.CurrencyRates
import java.util.Currency

fun CurrencyRatesCached.toDomain() = CurrencyRates(
    baseCurrency = baseCurrency,
    rates = rates.mapKeys { Currency.getInstance(it.key) },
    requestedAtInMillis = requestedAtInMillis
)