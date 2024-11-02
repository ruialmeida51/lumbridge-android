package com.eyther.lumbridge.mapper.loan

import com.eyther.lumbridge.domain.model.loan.LoanDomain
import com.eyther.lumbridge.domain.model.loan.LoanCategory
import com.eyther.lumbridge.domain.model.loan.LoanInterestRate
import com.eyther.lumbridge.domain.model.loan.LoanType
import com.eyther.lumbridge.model.loan.LoanCategoryUi
import com.eyther.lumbridge.model.loan.LoanInterestRateUi
import com.eyther.lumbridge.model.loan.LoanUi

fun LoanUi.toDomain() = LoanDomain(
    id = id,
    name = name,
    currentAmount = currentLoanAmount,
    initialAmount = initialLoanAmount,
    startDate = startDate,
    endDate = endDate,
    loanInterestRate = loanInterestRateUi.toDomain(),
    loanType = loanInterestRateUi.getLoanType(),
    loanCategory = loanCategoryUi.toDomain(),
    shouldNotifyWhenPaid = shouldNotifyWhenPaid,
    shouldAutoAddToExpenses = shouldAutoAddToExpenses,
    lastAutoPayDate = lastAutoPayDate,
    paymentDay = paymentDay
)

fun LoanInterestRateUi.getLoanType() = when(this) {
    is LoanInterestRateUi.Fixed.Taeg -> LoanType.TAEG
    is LoanInterestRateUi.Fixed.Tan -> LoanType.FIXED_TAN
    is LoanInterestRateUi.Variable -> LoanType.EURIBOR_VARIABLE
}

fun LoanInterestRateUi.toDomain() = LoanInterestRate.fromLoanType(
    loanType = getLoanType(),
    variableEuribor = tryGetEuribor(),
    variableSpread = tryGetSpread(),
    fixedTanInterestRate = tryGetTanInterestRate(),
    fixedTaegInterestRate = tryGetTaegInterestRate()
)

fun LoanCategoryUi.toDomain() = when (this) {
    LoanCategoryUi.House -> LoanCategory.HOUSE
    LoanCategoryUi.Car -> LoanCategory.CAR
    LoanCategoryUi.Personal -> LoanCategory.PERSONAL
    LoanCategoryUi.Other -> LoanCategory.OTHER
}
