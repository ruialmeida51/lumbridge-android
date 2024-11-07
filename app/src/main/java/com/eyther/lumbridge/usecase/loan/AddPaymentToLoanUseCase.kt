package com.eyther.lumbridge.usecase.loan

import com.eyther.lumbridge.domain.repository.loan.LoanRepository
import com.eyther.lumbridge.mapper.loan.toDomain
import com.eyther.lumbridge.model.loan.LoanCalculationUi
import com.eyther.lumbridge.model.loan.LoanUi
import javax.inject.Inject

class AddPaymentToLoanUseCase @Inject constructor(
    private val loanRepository: LoanRepository
) {
    /**
     * Attempts to add a monthly payment to the user's loan. This is done by
     * subtracting the monthly payment capital from the loan amount and incrementing
     * the start date by one month.
     *
     * @param loanUi The loan UI to add the amortization to.
     */
    suspend operator fun invoke(loanUi: LoanUi, loanCalculationUi: LoanCalculationUi) {
        val loan = loanUi.toDomain()
        val newAmount = loan.currentAmount - loanCalculationUi.monthlyPaymentCapital
        val newStartDate = loan.currentPaymentDate.plusMonths(1)

        loanRepository.saveLoan(
            loan.copy(
                currentAmount = newAmount,
                currentPaymentDate = newStartDate
            )
        )
    }
}
