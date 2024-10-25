package com.eyther.lumbridge.features.editloan.model

import androidx.annotation.StringRes
import com.eyther.lumbridge.R

sealed class EditLoanFixedTypeUi(
    @StringRes val label: Int,
    val ordinal: Int
) {
    companion object {
        fun entries() = listOf(Tan, Taeg)
    }

    data object Tan : EditLoanFixedTypeUi(
        label = R.string.tan,
        ordinal = 0
    )

    data object Taeg : EditLoanFixedTypeUi(
        label = R.string.taeg,
        ordinal = 1
    )
}
