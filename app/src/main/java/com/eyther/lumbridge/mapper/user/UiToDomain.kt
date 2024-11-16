package com.eyther.lumbridge.mapper.user

import com.eyther.lumbridge.domain.model.netsalary.deduction.DuodecimosType
import com.eyther.lumbridge.domain.model.netsalary.salarypaymenttype.SalaryInputType
import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain
import com.eyther.lumbridge.domain.model.user.UserProfileDomain
import com.eyther.lumbridge.model.finance.DuodecimosTypeUi
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
    },
    duodecimosType = duodecimosTypeUi.toDomain()
)

fun DuodecimosTypeUi.toDomain() = when(this) {
    DuodecimosTypeUi.TwelveMonths -> DuodecimosType.TWELVE_MONTHS
    DuodecimosTypeUi.ThirteenMonths -> DuodecimosType.THIRTEEN_MONTHS
    DuodecimosTypeUi.FourteenMonths -> DuodecimosType.FOURTEEN_MONTHS
}
