package com.eyther.lumbridge.data.model.currencyexchange.remote

import com.google.gson.annotations.SerializedName

data class CurrencyRatesRemote(
    @SerializedName("status_code")
    val statusCode: Int? = null,

    @SerializedName("data")
    val data: CurrencyRatesDataRemote? = null
)

data class CurrencyRatesDataRemote(
    @SerializedName("base")
    val base: String? = null,

    @SerializedName("target")
    val target: String? = null,

    @SerializedName("mid")
    val rate: Double? = null,

    @SerializedName("unit")
    val unit: Int? = null,

    @SerializedName("timestamp")
    val timestamp: String? = null
)
