package com.eyther.lumbridge.usecase.loan

import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.domain.model.loan.LoanCalculation
import com.eyther.lumbridge.domain.model.loan.LoanDomain
import com.eyther.lumbridge.domain.repository.loan.LoanRepository
import javax.inject.Inject

class GetAllLoansUseCase @Inject constructor(
    private val loanRepository: LoanRepository
) {
    suspend operator fun invoke(locale: SupportedLocales): List<Pair<LoanDomain, LoanCalculation>> =
        loanRepository.getLoansAndCalculations(locale)
}
