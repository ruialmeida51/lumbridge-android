package com.eyther.lumbridge.domain.mapper.user

import com.eyther.lumbridge.data.model.user.local.UserFinancialsCached
import com.eyther.lumbridge.data.model.user.local.UserMortgageCached
import com.eyther.lumbridge.data.model.user.local.UserProfileCached
import com.eyther.lumbridge.domain.model.netsalary.deduction.DuodecimosType
import com.eyther.lumbridge.domain.model.netsalary.salarypaymenttype.SalaryInputType
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain
import com.eyther.lumbridge.domain.model.user.UserMortgageDomain
import com.eyther.lumbridge.domain.model.user.UserProfileDomain
import com.eyther.lumbridge.shared.time.extensions.toLocalDate

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
    salaryInputType = SalaryInputType.valueOf(salaryInputType),
    duodecimosType = duodecimosType?.let { DuodecimosType.valueOf(it) } ?: DuodecimosType.FOURTEEN_MONTHS
)

fun UserMortgageCached.toDomain() = UserMortgageDomain(
    loanAmount = loanAmount,
    euribor = euribor,
    spread = spread,
    fixedInterestRate = fixedInterestRate,
    startDate = startDate.toLocalDate(),
    endDate = endDate.toLocalDate()
)
