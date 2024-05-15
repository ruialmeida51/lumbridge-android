package com.eyther.lumbridge.domain.model.finance.mortgage

data class MortgageAmortization(
    val amortization: Float,
    val remainder: Float,
    val nextPayment: Float
)
