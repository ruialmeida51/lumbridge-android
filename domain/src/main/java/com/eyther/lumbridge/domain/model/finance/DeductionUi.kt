package com.eyther.lumbridge.domain.model.finance

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeductionUi(
    val percentage: String?,
    val amount: Float,
    @StringRes val label: Int
): Parcelable {
    fun hasPercentage() = percentage != null
}
