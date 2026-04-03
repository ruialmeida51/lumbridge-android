package com.eyther.lumbridge.model.loan

import androidx.annotation.StringRes
import com.eyther.lumbridge.R

sealed class LoanInterestRateUi(
    @StringRes val label: Int,
    val ordinal: Int
) {
    sealed class Fixed(
        @StringRes label: Int,
        ordinal: Int,
        open val interestRate: Float
    ): LoanInterestRateUi(label, ordinal) {

        data class Tan(
            override val interestRate: Float
        ): Fixed(R.string.tan, 0, interestRate)
        
        data class Taeg(
            override val interestRate: Float
        ): Fixed(R.string.taeg, 1, interestRate)
    }
    
    data class Variable(
        val spread: Float,
        val euribor: Float
    ): LoanInterestRateUi(R.string.variable, 2)

    fun tryGetTanInterestRate() = (this as? Fixed.Tan)?.interestRate
    fun tryGetTaegInterestRate() = (this as? Fixed.Taeg)?.interestRate
    fun tryGetEuribor() = (this as? Variable)?.euribor
    fun tryGetSpread() = (this as? Variable)?.spread
    fun asFixed(): Fixed? = this as? Fixed


}
