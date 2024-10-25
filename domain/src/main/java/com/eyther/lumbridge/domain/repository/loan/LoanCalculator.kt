package com.eyther.lumbridge.domain.repository.loan

import com.eyther.lumbridge.domain.model.loan.Loan
import com.eyther.lumbridge.domain.model.loan.LoanCalculation

interface LoanCalculator {
    /**
     * Calculates the loan and returns the monthly payment and the total amount paid,
     * amongst other meta data about the loan.
     *
     * @param loan the information about the loan
     * @return the loan calculation
     */
    suspend fun calculate(loan: Loan): LoanCalculation
}
