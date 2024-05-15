package com.eyther.lumbridge.domain.model.finance.mortgage

data class MortgageCalculation(
    val loanAmount: Float,
    val monthlyPayment: Float,
    val monthlyPaymentCapital: Float,
    val monthsLeft: Int,
    val monthlyPaymentInterest: Float,
    val euribor: Float?,
    val spread: Float?,
    val fixedInterestRate: Float?,
    val mortgageType: MortgageType,
    val amortizations: List<MortgageAmortization>
)
