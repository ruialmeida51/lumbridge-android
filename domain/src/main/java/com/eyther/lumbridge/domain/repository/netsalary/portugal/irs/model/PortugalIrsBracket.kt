package com.eyther.lumbridge.domain.repository.netsalary.portugal.irs.model

data class PortugalIrsBracket(
    val irsDeductionValue: Float,
    val irsBracketPercentage: Float,
    val ssDeductionValue: Float,
    val ssDeductionPercentage: Float,
    val flatRate: Float,
    val netSalary: Float
)
