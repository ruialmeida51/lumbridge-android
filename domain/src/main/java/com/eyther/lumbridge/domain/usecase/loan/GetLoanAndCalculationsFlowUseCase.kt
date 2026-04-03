package com.eyther.lumbridge.domain.usecase.loan

import com.eyther.lumbridge.domain.model.loan.LoanCalculation
import com.eyther.lumbridge.domain.model.loan.LoanDomain
import com.eyther.lumbridge.domain.repository.loan.LoanRepository
import com.eyther.lumbridge.domain.usecase.user.profile.GetLocaleOrDefault
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLoanAndCalculationsFlowUseCase @Inject constructor(
    private val loanRepository: LoanRepository,
    private val getLocaleOrDefault: GetLocaleOrDefault
) {
    suspend operator fun invoke(loanId: Long): Flow<Pair<LoanDomain, LoanCalculation>> = loanRepository
        .getLoanAndCalculationByIdStream(
            loanId = loanId,
            locale = getLocaleOrDefault()
        )
}
