package com.eyther.lumbridge.mapper.loan

import com.eyther.lumbridge.domain.model.loan.Loan
import com.eyther.lumbridge.domain.model.loan.LoanAmortization
import com.eyther.lumbridge.domain.model.loan.LoanCalculation
import com.eyther.lumbridge.domain.model.loan.LoanCategory
import com.eyther.lumbridge.domain.model.loan.LoanInterestRate
import com.eyther.lumbridge.model.loan.LoanAmortizationUi
import com.eyther.lumbridge.model.loan.LoanCalculationUi
import com.eyther.lumbridge.model.loan.LoanCategoryUi
import com.eyther.lumbridge.model.loan.LoanInterestRateUi
import com.eyther.lumbridge.model.loan.LoanUi

fun Loan.toUi() = LoanUi(
    id = id,
    name = name,
    currentLoanAmount = currentAmount,
    initialLoanAmount = initialAmount,
    startDate = startDate,
    endDate = endDate,
    loanCategoryUi = loanCategory.toUi(),
    loanInterestRateUi = loanInterestRate.toUi(),
    shouldNotifyWhenPaid = shouldNotifyWhenPaid,
    shouldAutoAddToExpenses = shouldAutoAddToExpenses,
    lastAutoPayDate = lastAutoPayDate,
    paymentDay = paymentDay
)

fun LoanCalculation.toUi() = LoanCalculationUi(
    monthlyPayment = monthlyPayment,
    monthlyPaymentCapital = monthlyPaymentCapital,
    monthlyPaymentInterest = monthlyPaymentInterest,
    remainingMonths = remainingMonths,
    amortizationUi = amortizations.map { it.toUi() }
)

fun LoanInterestRate.toUi() = when (this) {
    is LoanInterestRate.FixedTan -> LoanInterestRateUi.Fixed.Tan(fixedInterestRate)
    is LoanInterestRate.EuriborVariable -> LoanInterestRateUi.Variable(euribor, spread)
    is LoanInterestRate.Taeg -> LoanInterestRateUi.Fixed.Taeg(taegInterestRate)
}

fun LoanCategory.toUi() = when (this) {
    LoanCategory.HOUSE -> LoanCategoryUi.House
    LoanCategory.CAR -> LoanCategoryUi.Car
    LoanCategory.PERSONAL -> LoanCategoryUi.Personal
    LoanCategory.OTHER -> LoanCategoryUi.Other
}

fun LoanAmortization.toUi() = LoanAmortizationUi(
    amortization = amortization,
    remainder = remainder,
    nextPayment = nextPayment
)
