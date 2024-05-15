package com.eyther.lumbridge.domain.mapper.user

import com.eyther.lumbridge.data.model.user.UserFinancialsCached
import com.eyther.lumbridge.data.model.user.UserMortgageCached
import com.eyther.lumbridge.data.model.user.UserProfileCached
import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain
import com.eyther.lumbridge.domain.model.user.UserMortgageDomain
import com.eyther.lumbridge.domain.model.user.UserProfileDomain
import com.eyther.lumbridge.domain.time.toIsoLocalDateString

fun UserProfileDomain.toCached() = UserProfileCached(
    countryCode = locale.countryCode,
    name = name,
    email = email
)

fun UserFinancialsDomain.toCached() = UserFinancialsCached(
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

fun UserMortgageDomain.toCached() = UserMortgageCached(
    mortgageType = mortgageType.name,
    loanAmount = loanAmount,
    euribor = euribor,
    spread = spread,
    fixedInterestRate = fixedInterestRate,
    startDate = startDate.toIsoLocalDateString(),
    endDate = endDate.toIsoLocalDateString()
)
