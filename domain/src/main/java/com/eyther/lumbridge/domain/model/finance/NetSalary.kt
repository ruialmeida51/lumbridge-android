package com.eyther.lumbridge.domain.model.finance

import com.eyther.lumbridge.domain.model.finance.allocation.MoneyAllocation
import com.eyther.lumbridge.domain.model.finance.deduction.Deduction

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
