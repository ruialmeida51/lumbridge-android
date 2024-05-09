package com.eyther.lumbridge.domain.mapper.user

import com.eyther.lumbridge.data.model.user.UserFinancialsCached
import com.eyther.lumbridge.data.model.user.UserMortgageCached
import com.eyther.lumbridge.data.model.user.UserProfileCached
import com.eyther.lumbridge.domain.model.finance.MortgageType
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain
import com.eyther.lumbridge.domain.model.user.UserMortgageDomain
import com.eyther.lumbridge.domain.model.user.UserProfileDomain

fun UserProfileCached.toDomain() = UserProfileDomain(
    name = name,
    email = email,
    locale = SupportedLocales.get(countryCode),
)

fun UserFinancialsCached.toDomain() = UserFinancialsDomain(
    annualGrossSalary = annualGrossSalary,
    foodCardPerDiem = foodCardPerDiem,
    savingsPercentage = savingsPercentage,
    necessitiesPercentage = necessitiesPercentage,
    luxuriesPercentage = luxuriesPercentage,
    numberOfDependants = numberOfDependants,
    singleIncome = singleIncome,
    married = married,
    handicapped = handicapped,
)

fun UserMortgageCached.toDomain() = UserMortgageDomain(
    mortgageType = MortgageType.valueOf(mortgageType),
    loanAmount = loanAmount,
    monthsLeft = monthsLeft,
    euribor = euribor,
    spread = spread,
    fixedInterestRate = fixedInterestRate,
)
