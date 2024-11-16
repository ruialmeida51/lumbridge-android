package com.eyther.lumbridge.data.model.user.local

data class UserFinancialsCached(
    val annualGrossSalary: Float,
    val salaryInputType: String,
    val duodecimosType: String?,
    val foodCardPerDiem: Float,
    val savingsPercentage: Int?,
    val necessitiesPercentage: Int?,
    val luxuriesPercentage: Int?,
    val numberOfDependants: Int?,
    val singleIncome: Boolean,
    val married: Boolean,
    val handicapped: Boolean
)
