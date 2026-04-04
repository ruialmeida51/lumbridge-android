package com.eyther.lumbridge.domain.usecase.loan

import com.eyther.lumbridge.domain.model.loan.LoanCalculation
import com.eyther.lumbridge.domain.model.loan.LoanDomain
import com.eyther.lumbridge.domain.repository.loan.LoanRepository
import javax.inject.Inject

class AddPaymentToLoanUseCase @Inject constructor(
    private val loanRepository: LoanRepository
) {
    /**
     * Attempts to add a monthly payment to the user's loan. This is done by
     * subtracting the monthly payment capital from the loan amount and incrementing
     * the start date by one month.
     *
     * @param loanDomain The loan to add the payment to.
     */
    suspend operator fun invoke(loanDomain: LoanDomain, loanCalculation: LoanCalculation) {
        val newAmount = loanDomain.currentAmount - loanCalculation.monthlyPaymentCapital
        val newStartDate = loanDomain.currentPaymentDate.plusMonths(1)

        loanRepository.saveLoan(
            loanDomain.copy(
                currentAmount = newAmount,
                currentPaymentDate = newStartDate
            )
        )
    }
}
