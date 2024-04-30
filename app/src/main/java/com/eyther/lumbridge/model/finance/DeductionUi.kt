package com.eyther.lumbridge.model.finance

data class DeductionUi(
    val percentage: String?,
    val amount: Float,
    val label: String
) {
    fun hasPercentage() = percentage != null
}
