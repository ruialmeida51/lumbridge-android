package com.eyther.lumbridge.usecase.loan

import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.domain.model.loan.LoanCalculation
import com.eyther.lumbridge.domain.model.loan.LoanDomain
import com.eyther.lumbridge.domain.repository.loan.LoanRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLoansFlowUseCase @Inject constructor(
    private val loanRepository: LoanRepository
) {
    operator fun invoke(locale: SupportedLocales): Flow<List<Pair<LoanDomain, LoanCalculation>>> =
        loanRepository.getLoansAndCalculationsFlow(locale)
}
