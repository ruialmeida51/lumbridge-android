package com.eyther.lumbridge.domain.usecase.loan

import com.eyther.lumbridge.domain.model.loan.LoanDomain
import com.eyther.lumbridge.domain.repository.loan.LoanRepository
import javax.inject.Inject

class SaveLoanUseCase @Inject constructor(
    private val loanRepository: LoanRepository
) {
    suspend operator fun invoke(loanDomain: LoanDomain) {
        loanRepository.saveLoan(loanDomain)
    }
}
