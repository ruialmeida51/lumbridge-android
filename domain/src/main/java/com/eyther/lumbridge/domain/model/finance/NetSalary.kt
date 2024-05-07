package com.eyther.lumbridge.domain.model.finance

data class NetSalary(
    val annualGrossSalary: Float,
    val annualNetSalary: Float,
    val monthlyGrossSalary: Float,
    val monthlyNetSalary: Float,
    val monthlyFoodCard: Float,
    val dailyFoodCard: Float,
    val deductions: List<Deduction>,
    val moneyAllocation: List<MoneyAllocation>? = null
)
