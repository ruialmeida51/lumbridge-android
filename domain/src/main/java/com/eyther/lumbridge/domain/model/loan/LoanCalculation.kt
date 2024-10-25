package com.eyther.lumbridge.domain.model.loan

data class LoanCalculation(
    val loanAmount: Float,
    val monthlyPayment: Float,
    val monthlyPaymentCapital: Float,
    val remainingMonths: Int,
    val monthlyPaymentInterest: Float,
    val loanType: LoanType,
    val loanCategory: LoanCategory,
    val amortizations: List<LoanAmortization>
)
