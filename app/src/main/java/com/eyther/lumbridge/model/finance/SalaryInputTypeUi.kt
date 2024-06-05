package com.eyther.lumbridge.model.finance

import androidx.annotation.StringRes
import com.eyther.lumbridge.R

sealed class SalaryInputTypeUi(@StringRes val label: Int, val ordinal: Int) {
    companion object {
        fun entries() = listOf(Monthly, Annually)

        fun fromOrdinal(ordinal: Int) = when (ordinal) {
            Monthly.ordinal -> Monthly
            Annually.ordinal -> Annually
            else -> throw IllegalArgumentException("💥 Unknown ordinal: $ordinal")
        }
    }

    data object Monthly : SalaryInputTypeUi(
        label = R.string.gross_monthly,
        ordinal = 0
    )

    data object Annually : SalaryInputTypeUi(
        label = R.string.gross_annual,
        ordinal = 1
    )
}
