package com.eyther.lumbridge.usecase.loan

import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.domain.repository.loan.LoanRepository
import com.eyther.lumbridge.mapper.loan.toUi
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllLoansUseCase @Inject constructor(
    private val loanRepository: LoanRepository
) {
    suspend operator fun invoke(locale: SupportedLocales) = loanRepository
        .getLoansAndCalculations(locale)
        .map { (loan, loanCalculation) -> loan.toUi() to loanCalculation.toUi() }
}
