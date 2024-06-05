package com.eyther.lumbridge.mapper.user

import com.eyther.lumbridge.domain.model.finance.mortgage.MortgageType
import com.eyther.lumbridge.domain.model.finance.salarypaymenttype.SalaryInputType
import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain
import com.eyther.lumbridge.domain.model.user.UserMortgageDomain
import com.eyther.lumbridge.domain.model.user.UserProfileDomain
import com.eyther.lumbridge.model.finance.SalaryInputTypeUi
import com.eyther.lumbridge.model.mortgage.MortgageTypeUi
import com.eyther.lumbridge.model.user.UserFinancialsUi
import com.eyther.lumbridge.model.user.UserMortgageUi
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
    savingsPercentage = savingsPercentage,
    necessitiesPercentage = necessitiesPercentage,
    luxuriesPercentage = luxuriesPercentage,
    numberOfDependants = numberOfDependants,
    singleIncome = singleIncome,
    married = married,
    handicapped = handicapped,
    salaryInputTypeUi = when (salaryInputType) {
        SalaryInputType.MONTHLY -> SalaryInputTypeUi.Monthly
        SalaryInputType.ANNUALLY -> SalaryInputTypeUi.Annually
    }
)

fun UserMortgageDomain.toUi() = UserMortgageUi(
    mortgageType = when (mortgageType) {
        MortgageType.FIXED -> MortgageTypeUi.Fixed
        MortgageType.VARIABLE -> MortgageTypeUi.Variable
    },
    loanAmount = loanAmount,
    euribor = euribor,
    spread = spread,
    fixedInterestRate = fixedInterestRate,
    startDate = startDate,
    endDate = endDate
)
