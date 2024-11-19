package com.eyther.lumbridge.data.model.netsalary.files.portugal

import com.google.gson.annotations.SerializedName

data class PortugalIrsTableFromFile(
    @SerializedName("taxPercentage")
    val taxPercentageFromFile: List<PortugalTaxPercentageFromFile>,
    @SerializedName("flatRate")
    val flatRateFromFile: List<PortugalFlatRateFromFile>
)

data class PortugalTaxPercentageFromFile(
    @SerializedName("range")
    val range: List<Float>,
    @SerializedName("value")
    val percentage: Float
)

data class PortugalFlatRateFromFile(
    @SerializedName("range")
    val range: List<Float>,
    @SerializedName("value")
    val rate: Float? = null,
    @SerializedName("formula")
    val formula: PortugalFlatRateFormulaFromFile? = null,
    @SerializedName("perDependentAmount")
    val perDependentAmount: Float
)

data class PortugalFlatRateFormulaFromFile(
    @SerializedName("taxPercentage")
    val taxPercentage: Float,
    @SerializedName("multiplier")
    val multiplier: Float,
    @SerializedName("rate")
    val rate: Float
)
