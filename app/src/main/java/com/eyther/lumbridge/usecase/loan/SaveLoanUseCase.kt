package com.eyther.lumbridge.usecase.loan

import com.eyther.lumbridge.domain.repository.loan.LoanRepository
import com.eyther.lumbridge.mapper.loan.toDomain
import com.eyther.lumbridge.model.loan.LoanUi
import javax.inject.Inject

class SaveLoanUseCase @Inject constructor(
    private val loanRepository: LoanRepository
) {
    suspend operator fun invoke(loanUi: LoanUi) {
        loanRepository.saveLoan(loanUi.toDomain())
    }
}
