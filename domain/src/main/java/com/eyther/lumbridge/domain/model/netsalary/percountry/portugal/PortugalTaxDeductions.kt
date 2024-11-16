package com.eyther.lumbridge.domain.model.netsalary.percountry.portugal

data class PortugalTaxDeductions(
    val irsDeductionValue: Float,
    val irsBracketPercentage: Float,
    val ssDeductionValue: Float,
    val ssDeductionPercentage: Float,
    val netSalary: Float
)
