package com.eyther.lumbridge.features.overview.breakdown.model

data class BalanceSheetNetUi(
    val moneyIn: Float,
    val moneyOut: Float,
    val net: Float
) {
    val percentageSpent: Float = minOf(moneyIn / (moneyOut + moneyIn), 1f)
}
