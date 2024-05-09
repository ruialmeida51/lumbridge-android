package com.eyther.lumbridge.domain.model.finance

data class MortgageCalculation(
    val loanAmount: Float,
    val monthlyPayment: Float,
    val remainingAmount: Float,
    val monthsLeft: Int,
    val totalPaid: Float,
    val euribor: Float?,
    val spread: Float?,
    val fixedInterestRate: Float?,
    val mortgageType: MortgageType
)
