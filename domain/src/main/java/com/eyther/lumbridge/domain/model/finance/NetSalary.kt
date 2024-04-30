package com.eyther.lumbridge.domain.model.finance

data class NetSalary(
    val salary: Float,
    val foodCard: Float,
    val deductions: List<Deduction>
)
