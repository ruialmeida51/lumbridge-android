package com.eyther.lumbridge.mapper.user

import com.eyther.lumbridge.domain.model.netsalary.salarypaymenttype.SalaryInputType
import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain
import com.eyther.lumbridge.domain.model.user.UserProfileDomain
import com.eyther.lumbridge.mapper.finance.toUi
import com.eyther.lumbridge.model.finance.SalaryInputTypeUi
import com.eyther.lumbridge.model.user.UserFinancialsUi
import com.eyther.lumbridge.model.user.UserProfileUi

fun UserProfileDomain.toUi() = UserProfileUi(
    locale = locale,
    name = name,
    email = email,
    imageBitmap = imageBitmap
)

fun UserFinancialsDomain.toUi() = UserFinancialsUi(
    annualGrossSalary = annualGrossSalary,
    foodCardPerDiem = foodCardPerDiem,
    // In the UI, we use percentages as integers from 0 to 100%, but we store it as a float from 0 to 1
    savingsPercentage = savingsPercentage?.times(100f)?.toInt(),
    necessitiesPercentage = necessitiesPercentage?.times(100f)?.toInt(),
    luxuriesPercentage = luxuriesPercentage?.times(100f)?.toInt(),
    numberOfDependants = numberOfDependants,
    singleIncome = singleIncome,
    married = married,
    handicapped = handicapped,
    salaryInputTypeUi = when (salaryInputType) {
        SalaryInputType.MONTHLY -> SalaryInputTypeUi.Monthly
        SalaryInputType.ANNUALLY -> SalaryInputTypeUi.Annually
    },
    duodecimosTypeUi = duodecimosType.toUi()
)
