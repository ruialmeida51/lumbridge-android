package com.eyther.lumbridge.model.finance

data class NetSalaryUi(
    val salary: Float,
    val foodCard: Float,
    val deductions: List<DeductionUi>
)
