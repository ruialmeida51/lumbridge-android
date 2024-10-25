package com.eyther.lumbridge.usecase.loan

import com.eyther.lumbridge.domain.repository.loan.LoanRepository
import com.eyther.lumbridge.mapper.loan.toUi
import com.eyther.lumbridge.model.loan.LoanCalculationUi
import com.eyther.lumbridge.model.loan.LoanUi
import com.eyther.lumbridge.usecase.user.profile.GetLocaleOrDefault
import javax.inject.Inject

class GetLoanAndCalculationsUseCase @Inject constructor(
    private val loanRepository: LoanRepository,
    private val getLocaleOrDefault: GetLocaleOrDefault
) {
    suspend operator fun invoke(loanId: Long): Pair<LoanUi?, LoanCalculationUi?> {
        val locale = getLocaleOrDefault()
        val loan = loanRepository.getLoanById(loanId)
        val loanCalculation = loan?.let { loanRepository.calculate(it, locale) }

        return loan?.toUi() to loanCalculation?.toUi()
    }
}
