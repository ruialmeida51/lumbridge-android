package com.eyther.lumbridge.domain.model.loan

sealed class LoanInterestRate(val value: Float) {
    companion object {
        fun fromLoanType(
            loanType: LoanType,
            variableEuribor: Float?,
            variableSpread: Float?,
            fixedTanInterestRate: Float?,
            fixedTaegInterestRate: Float?
        ): LoanInterestRate {
            return when (loanType) {
                LoanType.FIXED_TAN -> FixedTan(
                    checkNotNull(fixedTanInterestRate) { "💥 Fixed interest rate is required for FIXED_TAN loan type" }
                )
                LoanType.EURIBOR_VARIABLE -> EuriborVariable(
                    checkNotNull(variableEuribor) { "💥 Euribor is required for EURIBOR_VARIABLE loan type" },
                    checkNotNull(variableSpread) { "💥 Spread is required for EURIBOR_VARIABLE loan type" }
                )
                LoanType.TAEG -> Taeg(
                    checkNotNull(fixedTaegInterestRate) { "💥 TAEG interest rate is required for TAEG loan type" }
                )
            }
        }
    }

    data class FixedTan(
        val fixedInterestRate: Float
    ) : LoanInterestRate(fixedInterestRate)

    data class EuriborVariable(
        val euribor: Float,
        val spread: Float
    ) : LoanInterestRate(euribor + spread)

    data class Taeg(
        val taegInterestRate: Float
    ) : LoanInterestRate(taegInterestRate)

    fun tryGetTanInterestRate() = (this as? FixedTan)?.fixedInterestRate
    fun tryGetEuribor() = (this as? EuriborVariable)?.euribor
    fun tryGetSpread() = (this as? EuriborVariable)?.spread
    fun tryGetTaegInterestRate() = (this as? Taeg)?.taegInterestRate
}
