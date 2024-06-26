package com.eyther.lumbridge.domain.mapper.user

import com.eyther.lumbridge.data.model.user.local.UserFinancialsCached
import com.eyther.lumbridge.data.model.user.local.UserMortgageCached
import com.eyther.lumbridge.data.model.user.local.UserProfileCached
import com.eyther.lumbridge.domain.model.finance.mortgage.MortgageType
import com.eyther.lumbridge.domain.model.finance.salarypaymenttype.SalaryInputType
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain
import com.eyther.lumbridge.domain.model.user.UserMortgageDomain
import com.eyther.lumbridge.domain.model.user.UserProfileDomain
import com.eyther.lumbridge.domain.time.toLocalDate

fun UserProfileCached.toDomain() = UserProfileDomain(
    name = name,
    email = email,
    imageBitmap = imageBitmap,
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
    salaryInputType = SalaryInputType.valueOf(salaryInputType)
)

fun UserMortgageCached.toDomain() = UserMortgageDomain(
    mortgageType = MortgageType.valueOf(mortgageType),
    loanAmount = loanAmount,
    euribor = euribor,
    spread = spread,
    fixedInterestRate = fixedInterestRate,
    startDate = startDate.toLocalDate(),
    endDate = endDate.toLocalDate()
)
