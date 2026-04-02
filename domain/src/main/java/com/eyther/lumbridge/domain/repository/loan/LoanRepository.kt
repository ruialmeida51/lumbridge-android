package com.eyther.lumbridge.domain.repository.loan

import com.eyther.lumbridge.domain.model.loan.LoanCalculation
import com.eyther.lumbridge.domain.model.loan.LoanDomain
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.domain.model.user.UserMortgageDomain
import kotlinx.coroutines.flow.Flow

interface LoanRepository {
    fun getLoansAndCalculationsFlow(locale: SupportedLocales): Flow<List<Pair<LoanDomain, LoanCalculation>>>
    fun getLoanAndCalculationByIdStream(loanId: Long, locale: SupportedLocales): Flow<Pair<LoanDomain, LoanCalculation>>
    suspend fun getLoansAndCalculations(locale: SupportedLocales): List<Pair<LoanDomain, LoanCalculation>>
    suspend fun saveLoan(loanDomain: LoanDomain)
    suspend fun deleteLoanById(loanId: Long)
    suspend fun getLoanById(loanId: Long): LoanDomain?
    suspend fun calculate(loanDomain: LoanDomain, locale: SupportedLocales): LoanCalculation
    @Deprecated("Use Room instead. This will be removed in the future, for now it is only maintained for migration purposes.")
    suspend fun getMortgageLoan(): UserMortgageDomain?
}
