package com.eyther.lumbridge.model.mortgage

import androidx.annotation.StringRes
import com.eyther.lumbridge.R

sealed class MortgageTypeUi(@StringRes val label: Int) {
    data object Fixed: MortgageTypeUi(R.string.loan_type_fixed)
    data object Variable: MortgageTypeUi(R.string.loan_type_variable)

    fun isFixed() = this is Fixed
    fun isVariable() = this is Variable
}
