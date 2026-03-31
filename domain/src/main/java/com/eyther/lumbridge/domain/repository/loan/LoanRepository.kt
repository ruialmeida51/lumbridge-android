package com.eyther.lumbridge.domain.repository.loan

import com.eyther.lumbridge.domain.mapper.loan.toCached
import com.eyther.lumbridge.domain.mapper.loan.toDomain
import com.eyther.lumbridge.domain.mapper.user.toDomain
import com.eyther.lumbridge.domain.model.loan.LoanDomain
import com.eyther.lumbridge.domain.model.loan.LoanCalculation
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.domain.model.user.UserMortgageDomain
import com.eyther.lumbridge.domain.repository.loan.portugal.PortugalLoanCalculator
import com.eyther.lumbridge.shared.di.model.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface LoanRepository {
    fun getLoansAndCalculationsFlow(locale: SupportedLocales): Flow<List<Pair<LoanDomain, LoanCalculation>>>
    fun getLoanAndCalculationByIdStream( loanId: Long, locale: SupportedLocales ): Flow<Pair<LoanDomain, LoanCalculation>>
    suspend fun getLoansAndCalculations(locale: SupportedLocales): List<Pair<LoanDomain, LoanCalculation>>
    suspend fun saveLoan(loanDomain: LoanDomain)
    suspend fun deleteLoanById(loanId: Long)
    suspend fun getLoanById(loanId: Long): LoanDomain?
    suspend fun calculate( loanDomain: LoanDomain, locale: SupportedLocales ): LoanCalculation
    suspend fun getMortgageLoan(): UserMortgageDomain?
}
