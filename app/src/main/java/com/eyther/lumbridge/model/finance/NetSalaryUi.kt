package com.eyther.lumbridge.model.finance

data class NetSalaryUi(
    val monthlyGrossSalary: Float,
    val monthlyNetSalary: Float,
    val annualNetSalary: Float,
    val annualGrossSalary: Float,
    val monthlyFoodCard: Float,
    val dailyFoodCard: Float,
    val deductions: List<DeductionUi>,
    val moneyAllocations: List<MoneyAllocationUi>?
)
