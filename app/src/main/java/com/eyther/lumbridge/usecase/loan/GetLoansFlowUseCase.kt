package com.eyther.lumbridge.usecase.loan

import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.domain.repository.loan.LoanRepository
import com.eyther.lumbridge.mapper.loan.toUi
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLoansFlowUseCase @Inject constructor(
    private val loanRepository: LoanRepository
) {
    operator fun invoke(locale: SupportedLocales) = loanRepository
        .getLoansFlow(locale)
        .map { loans ->
            loans.map { (loan, loanCalculation) -> loan.toUi() to loanCalculation.toUi() }
        }
}
