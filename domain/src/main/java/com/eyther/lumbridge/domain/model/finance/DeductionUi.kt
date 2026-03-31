package com.eyther.lumbridge.domain.model.finance

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeductionUi(
    val percentage: String?,
    val amount: Float,
     val label: Int
): Parcelable {
    fun hasPercentage() = percentage != null
}
