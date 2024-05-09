package com.eyther.lumbridge.model.finance

import androidx.annotation.StringRes

data class DeductionUi(
    val percentage: String?,
    val amount: Float,
    @StringRes val label: Int
) {
    fun hasPercentage() = percentage != null
}
