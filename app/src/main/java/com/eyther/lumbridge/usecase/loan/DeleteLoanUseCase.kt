package com.eyther.lumbridge.usecase.loan

import com.eyther.lumbridge.domain.repository.loan.LoanRepository
import javax.inject.Inject

class DeleteLoanUseCase @Inject constructor(
    private val loanRepository: LoanRepository
) {
    suspend operator fun invoke(loanId: Long) = loanRepository.deleteLoanById(loanId)
}
