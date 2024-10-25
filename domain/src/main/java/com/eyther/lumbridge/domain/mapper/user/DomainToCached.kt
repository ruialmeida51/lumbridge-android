package com.eyther.lumbridge.domain.mapper.user

import com.eyther.lumbridge.data.model.user.local.UserFinancialsCached
import com.eyther.lumbridge.data.model.user.local.UserMortgageCached
import com.eyther.lumbridge.data.model.user.local.UserProfileCached
import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain
import com.eyther.lumbridge.domain.model.user.UserMortgageDomain
import com.eyther.lumbridge.domain.model.user.UserProfileDomain
import com.eyther.lumbridge.domain.time.toIsoLocalDateString

fun UserProfileDomain.toCached() = UserProfileCached(
    countryCode = locale.countryCode,
    name = name,
    email = email,
    imageBitmap = imageBitmap
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
    handicapped = handicapped,
    salaryInputType = salaryInputType.name
)

fun UserMortgageDomain.toCached() = UserMortgageCached(
    loanAmount = loanAmount,
    euribor = euribor,
    spread = spread,
    fixedInterestRate = fixedInterestRate,
    startDate = startDate.toIsoLocalDateString(),
    endDate = endDate.toIsoLocalDateString()
)
