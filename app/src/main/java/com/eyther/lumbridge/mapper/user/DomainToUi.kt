package com.eyther.lumbridge.mapper.user

import com.eyther.lumbridge.domain.model.finance.MortgageType
import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain
import com.eyther.lumbridge.domain.model.user.UserMortgageDomain
import com.eyther.lumbridge.domain.model.user.UserProfileDomain
import com.eyther.lumbridge.model.mortgage.MortgageTypeUi
import com.eyther.lumbridge.model.user.UserFinancialsUi
import com.eyther.lumbridge.model.user.UserMortgageUi
import com.eyther.lumbridge.model.user.UserProfileUi

fun UserProfileDomain.toUi() = UserProfileUi(
    locale = locale,
    name = name,
    email = email
)

fun UserFinancialsDomain.toUi() = UserFinancialsUi(
    annualGrossSalary = annualGrossSalary,
    foodCardPerDiem = foodCardPerDiem,
    savingsPercentage = savingsPercentage,
    necessitiesPercentage = necessitiesPercentage,
    luxuriesPercentage = luxuriesPercentage,
    numberOfDependants = numberOfDependants,
    singleIncome = singleIncome,
    married = married,
    handicapped = handicapped
)

fun UserMortgageDomain.toUi() = UserMortgageUi(
    mortgageType = when (mortgageType) {
        MortgageType.FIXED -> MortgageTypeUi.Fixed
        MortgageType.VARIABLE -> MortgageTypeUi.Variable
    },
    loanAmount = loanAmount,
    monthsLeft = monthsLeft,
    euribor = euribor,
    spread = spread,
    fixedInterestRate = fixedInterestRate
)
