package com.eyther.lumbridge.domain.model.loan

data class LoanAmortization(
    val amortization: Float,
    val remainder: Float,
    val nextPayment: Float
)
