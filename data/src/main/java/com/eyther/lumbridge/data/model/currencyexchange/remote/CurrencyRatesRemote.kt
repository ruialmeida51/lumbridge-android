package com.eyther.lumbridge.data.model.currencyexchange.remote

import com.google.gson.annotations.SerializedName

data class CurrencyRatesRemote(
    @SerializedName("provider")
    val provider: String? = null,

    @SerializedName("terms")
    val terms: String? = null,

    @SerializedName("base")
    val base: String? = null,

    @SerializedName("date")
    val date: String? = null,

    @SerializedName("time_last_updated")
    val timeLastUpdated: Int? = null,

    @SerializedName("rates")
    val rates: RatesRemote? = null
)
