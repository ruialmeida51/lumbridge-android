package com.eyther.lumbridge.domain.usecase.loan

import com.eyther.lumbridge.domain.repository.loan.LoanRepository
import com.eyther.lumbridge.domain.mapper.loan.toDomain
import com.eyther.lumbridge.domain.model.loan.LoanUi
import javax.inject.Inject

class SaveLoanUseCase @Inject constructor(
    private val loanRepository: LoanRepository
) {
    suspend operator fun invoke(loanUi: LoanUi) {
        loanRepository.saveLoan(loanUi.toDomain())
    }
}
