package com.eyther.lumbridge.data.model.netsalary.local.portugal

data class PortugalIrsTableCached(
    val taxPercentage: List<PortugalTaxPercentageCached>,
    val flatRate: List<PortugalFlatRateCached>
)

data class PortugalTaxPercentageCached(
    val range: List<Float>,
    val percentage: Float
) {
    init {
        require(range.size == 2) { "PortugalTaxPercentageFromFile range must have exactly 2 elements" }
    }
}

data class PortugalFlatRateCached(
    val range: List<Float>,
    val rate: Float? = null,
    val formula: PortugalFlatRateFormulaCached? = null,
    val perDependentAmount: Float
) {
    init {
        require(rate != null || formula != null) { "PortugalFlatRateFromFile must have either a rate or a formula" }
        require(range.size == 2) { "PortugalFlatRateFromFile range must have exactly 2 elements" }
    }
}

data class PortugalFlatRateFormulaCached(
    val taxPercentage: Float,
    val multiplier: Float,
    val rate: Float
)
