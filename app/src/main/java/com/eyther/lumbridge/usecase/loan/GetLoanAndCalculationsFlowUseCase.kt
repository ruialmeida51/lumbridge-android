package com.eyther.lumbridge.usecase.loan

import com.eyther.lumbridge.domain.repository.loan.LoanRepository
import com.eyther.lumbridge.mapper.loan.toUi
import com.eyther.lumbridge.model.loan.LoanCalculationUi
import com.eyther.lumbridge.model.loan.LoanUi
import com.eyther.lumbridge.usecase.user.profile.GetLocaleOrDefault
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLoanAndCalculationsFlowUseCase @Inject constructor(
    private val loanRepository: LoanRepository,
    private val getLocaleOrDefault: GetLocaleOrDefault
) {
    suspend operator fun invoke(loanId: Long): Flow<Pair<LoanUi, LoanCalculationUi>> = loanRepository
        .getLoanAndCalculationByIdStream(
            loanId = loanId,
            locale = getLocaleOrDefault()
        ).map { (loan, loanCalculation) ->
            loan.toUi() to loanCalculation.toUi()
        }
}
