package com.eyther.lumbridge.model.user

import com.eyther.lumbridge.model.finance.SalaryInputTypeUi

data class UserFinancialsUi(
    val salaryInputTypeUi: SalaryInputTypeUi,
    val annualGrossSalary: Float? = null,
    val foodCardPerDiem: Float? = null,

    val savingsPercentage: Int? = null,
    val necessitiesPercentage: Int? = null,
    val luxuriesPercentage: Int? = null,

    val numberOfDependants: Int? = null,
    val singleIncome: Boolean = false,
    val married: Boolean = false,
    val handicapped: Boolean = false
)
