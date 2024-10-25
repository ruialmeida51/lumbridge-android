package com.eyther.lumbridge.mapper.user

import com.eyther.lumbridge.domain.model.finance.salarypaymenttype.SalaryInputType
import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain
import com.eyther.lumbridge.domain.model.user.UserProfileDomain
import com.eyther.lumbridge.model.finance.SalaryInputTypeUi
import com.eyther.lumbridge.model.user.UserFinancialsUi
import com.eyther.lumbridge.model.user.UserProfileUi

fun UserProfileUi.toDomain() = UserProfileDomain(
    locale = locale,
    name = name,
    email = email,
    imageBitmap = imageBitmap
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
    handicapped = handicapped,
    salaryInputType = when(salaryInputTypeUi) {
        SalaryInputTypeUi.Monthly -> SalaryInputType.MONTHLY
        SalaryInputTypeUi.Annually -> SalaryInputType.ANNUALLY
    }
)
