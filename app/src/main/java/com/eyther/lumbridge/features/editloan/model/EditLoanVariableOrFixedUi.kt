package com.eyther.lumbridge.features.editloan.model

import androidx.annotation.StringRes
import com.eyther.lumbridge.R

sealed class EditLoanVariableOrFixedUi(
    @StringRes val label: Int,
    val ordinal: Int
) {
    companion object {
        fun entries() = listOf(Fixed, Variable)
    }

    data object Fixed : EditLoanVariableOrFixedUi(
        label = R.string.loan_type_fixed,
        ordinal = 0
    )

    data  object Variable: EditLoanVariableOrFixedUi(
        label = R.string.loan_type_variable,
        ordinal = 1
    )
}
