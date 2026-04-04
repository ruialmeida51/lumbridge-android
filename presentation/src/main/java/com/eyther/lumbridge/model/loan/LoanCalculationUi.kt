package com.eyther.lumbridge.model.loan

data class LoanCalculationUi(
    val monthlyPayment: Float,
    val monthlyPaymentCapital: Float,
    val monthlyPaymentInterest: Float,
    val remainingMonths: Int,
    val amortizationUi: List<LoanAmortizationUi>
)
