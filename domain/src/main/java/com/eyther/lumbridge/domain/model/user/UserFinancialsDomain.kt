package com.eyther.lumbridge.domain.model.user

data class UserFinancialsDomain(
    val annualGrossSalary: Float,
    val foodCardPerDiem: Float,
    val savingsPercentage: Int?,
    val necessitiesPercentage: Int?,
    val luxuriesPercentage: Int?,
    val numberOfDependants: Int?,
    val singleIncome: Boolean,
    val married: Boolean,
    val handicapped: Boolean
)