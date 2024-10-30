package com.eyther.lumbridge.features.editloan.model

import androidx.annotation.StringRes
import com.eyther.lumbridge.R

sealed class EditLoanFixedTypeChoice(
    @StringRes val label: Int,
    val ordinal: Int
) {
    companion object {
        fun entries() = listOf(Tan, Taeg)
    }

    data object Tan : EditLoanFixedTypeChoice(
        label = R.string.tan,
        ordinal = 0
    )

    data object Taeg : EditLoanFixedTypeChoice(
        label = R.string.taeg,
        ordinal = 1
    )
}
