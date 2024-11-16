package com.eyther.lumbridge.domain.model.netsalary

import com.eyther.lumbridge.domain.model.netsalary.allocation.MoneyAllocation
import com.eyther.lumbridge.domain.model.netsalary.deduction.Deduction
import com.eyther.lumbridge.domain.model.netsalary.deduction.DuodecimosType

data class NetSalary(
    val annualGrossSalary: Float,
    val annualNetSalary: Float,
    val monthlyGrossSalary: Float,
    val monthlyNetSalary: Float,
    val monthlyFoodCard: Float,
    val dailyFoodCard: Float,
    val duodecimosType: DuodecimosType,
    val deductions: List<Deduction>,
    val moneyAllocation: List<MoneyAllocation>? = null
)
