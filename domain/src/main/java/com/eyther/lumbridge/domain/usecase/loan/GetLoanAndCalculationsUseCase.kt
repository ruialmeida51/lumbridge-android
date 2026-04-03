package com.eyther.lumbridge.domain.usecase.loan

import com.eyther.lumbridge.domain.model.loan.LoanCalculation
import com.eyther.lumbridge.domain.model.loan.LoanDomain
import com.eyther.lumbridge.domain.repository.loan.LoanRepository
import com.eyther.lumbridge.domain.usecase.user.profile.GetLocaleOrDefault
import javax.inject.Inject

class GetLoanAndCalculationsUseCase @Inject constructor(
    private val loanRepository: LoanRepository,
    private val getLocaleOrDefault: GetLocaleOrDefault
) {
    suspend operator fun invoke(loanId: Long): Pair<LoanDomain?, LoanCalculation?> {
        val locale = getLocaleOrDefault()
        val loan = loanRepository.getLoanById(loanId)
        val loanCalculation = loan?.let { loanRepository.calculate(it, locale) }

        return loan to loanCalculation
    }
}
