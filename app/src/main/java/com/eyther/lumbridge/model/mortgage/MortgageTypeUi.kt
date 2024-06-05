package com.eyther.lumbridge.model.mortgage

import androidx.annotation.StringRes
import com.eyther.lumbridge.R

sealed class MortgageTypeUi(@StringRes val label: Int, val ordinal: Int) {
    companion object {
        fun entries() = listOf(Fixed, Variable)

        fun fromOrdinal(ordinal: Int) = when (ordinal) {
            Fixed.ordinal -> Fixed
            Variable.ordinal -> Variable
            else -> throw IllegalArgumentException("💥 Unknown ordinal: $ordinal")
        }
    }

    data object Fixed : MortgageTypeUi(
        label = R.string.loan_type_fixed,
        ordinal = 0
    )

    data object Variable : MortgageTypeUi(
        label = R.string.loan_type_variable,
        ordinal = 1
    )
}
