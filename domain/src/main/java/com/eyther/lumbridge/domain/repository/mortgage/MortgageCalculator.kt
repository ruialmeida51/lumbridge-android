package com.eyther.lumbridge.domain.repository.mortgage

import com.eyther.lumbridge.domain.model.finance.MortgageCalculation
import com.eyther.lumbridge.domain.model.user.UserMortgageDomain

interface MortgageCalculator {
    /**
     * Calculates the mortgage and returns the monthly payment and the total amount paid,
     * amongst other meta data about the mortgage.
     *
     * @param mortgageDomain the mortgage to calculate
     * @return the mortgage calculation
     */
    fun calculate(mortgageDomain: UserMortgageDomain): MortgageCalculation
}
