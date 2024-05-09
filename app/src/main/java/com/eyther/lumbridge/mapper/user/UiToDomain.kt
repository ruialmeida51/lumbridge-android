package com.eyther.lumbridge.mapper.user

import com.eyther.lumbridge.domain.model.finance.MortgageType
import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain
import com.eyther.lumbridge.domain.model.user.UserMortgageDomain
import com.eyther.lumbridge.domain.model.user.UserProfileDomain
import com.eyther.lumbridge.model.mortgage.MortgageTypeUi
import com.eyther.lumbridge.model.user.UserFinancialsUi
import com.eyther.lumbridge.model.user.UserMortgageUi
import com.eyther.lumbridge.model.user.UserProfileUi

fun UserProfileUi.toDomain() = UserProfileDomain(
    locale = locale,
    name = name,
    email = email
)

fun UserFinancialsUi.toDomain() = UserFinancialsDomain(
    annualGrossSalary = checkNotNull(annualGrossSalary),
    foodCardPerDiem = checkNotNull(foodCardPerDiem),
    savingsPercentage = savingsPercentage,
    necessitiesPercentage = necessitiesPercentage,
    luxuriesPercentage = luxuriesPercentage,
    numberOfDependants = numberOfDependants,
    singleIncome = singleIncome,
    married = married,
    handicapped = handicapped
)

fun UserMortgageUi.toDomain() = UserMortgageDomain(
    mortgageType = when (mortgageType) {
        MortgageTypeUi.Fixed -> MortgageType.FIXED
        MortgageTypeUi.Variable -> MortgageType.VARIABLE
    },
    loanAmount = loanAmount,
    monthsLeft = monthsLeft,
    euribor = euribor,
    spread = spread,
    fixedInterestRate = fixedInterestRate
)
