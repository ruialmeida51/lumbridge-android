package com.eyther.lumbridge.data.model.user.local

data class UserFinancialsCached(
    val annualGrossSalary: Float,
    val salaryInputType: String,
    val duodecimosType: String?,
    val foodCardPerDiem: Float,
    val savingsPercentage: Float?,
    val necessitiesPercentage: Float?,
    val luxuriesPercentage: Float?,
    val numberOfDependants: Int?,
    val singleIncome: Boolean,
    val married: Boolean,
    val handicapped: Boolean
)
