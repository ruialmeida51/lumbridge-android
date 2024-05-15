package com.eyther.lumbridge.model.mortgage

data class MortgageUi(
    val euribor: Float?,
    val spread: Float?,
    val fixedInterestRate: Float?,
    val loanAmount: Float,
    val monthsLeft: Int,
    val monthlyPayment: Float,
    val monthlyPaymentInterest: Float,
    val monthlyPaymentCapital: Float,
    val mortgageTypeUi: MortgageTypeUi,
    val amortizations: List<MortgageAmortizationUi>
) {
    init {
        when (mortgageTypeUi) {
            MortgageTypeUi.Fixed -> validateFixedInterestRate()
            MortgageTypeUi.Variable -> validateVariableInterestRate()
        }
    }

    /**
     * Validates the mortgage when the mortgage type is fixed. The fixed interest rate must be
     * present.
     * @throws IllegalArgumentException if the fixed interest rate is not present.
     */
    private fun validateFixedInterestRate() {
        checkNotNull(fixedInterestRate)
    }

    /**
     * Validates the mortgage when the mortgage type is variable. The euribor and spread must be
     * present.
     * @throws IllegalArgumentException if the euribor or spread is not present.
     */
    private fun validateVariableInterestRate() {
        checkNotNull(euribor)
        checkNotNull(spread)
    }
}
