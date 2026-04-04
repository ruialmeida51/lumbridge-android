package com.eyther.lumbridge.domain.model.finance

data class BalanceSheetDomain(
    val moneyIn: Float,
    val moneyOut: Float,
    val net: Float
)
