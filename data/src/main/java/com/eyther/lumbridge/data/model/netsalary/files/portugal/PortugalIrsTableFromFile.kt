package com.eyther.lumbridge.data.model.netsalary.files.portugal

import com.google.gson.annotations.SerializedName

data class PortugalIrsTableFromFile(
    @SerializedName("taxPercentage")
    val taxPercentageFromFile: List<PortugalTaxPercentageFromFile>,
    @SerializedName("flatRate")
    val flatRateFromFile: List<PortugalFlatRateFromFile>
)

data class PortugalTaxPercentageFromFile(
    val range: List<Float>,
    @SerializedName("value")
    val percentage: Float
)

data class PortugalFlatRateFromFile(
    val range: List<Float>,
    @SerializedName("value")
    val rate: Float? = null,
    val formula: PortugalFlatRateFormulaFromFile? = null,
    val perDependentAmount: Float
)

data class PortugalFlatRateFormulaFromFile(
    val taxPercentage: Float,
    val multiplier: Float,
    val rate: Float
)
