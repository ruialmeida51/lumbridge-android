package com.eyther.lumbridge.domain.repository.loan

import com.eyther.lumbridge.data.datasource.loan.local.LoanLocalDataSource
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

class LoanRepository @Inject constructor(
    private val portugalLoanCalculator: PortugalLoanCalculator,
    private val loanLocalDataSource: LoanLocalDataSource,
    private val schedulers: Schedulers
) {
    fun getLoansAndCalculationsFlow(locale: SupportedLocales): Flow<List<Pair<LoanDomain, LoanCalculation>>> =
        loanLocalDataSource
            .loansFlow
            .mapNotNull { cachedLoans ->
                cachedLoans
                    ?.toDomain()
                    ?.map { it to calculate(it, locale) }
            }

    fun getLoanAndCalculationByIdStream(
        loanId: Long,
        locale: SupportedLocales
    ): Flow<Pair<LoanDomain, LoanCalculation>> =
        loanLocalDataSource
            .getLoanByIdStream(loanId)
            .mapNotNull { cachedLoan ->
                cachedLoan
                    ?.toDomain()
                    ?.let { it to calculate(it, locale) }
            }

    suspend fun getLoansAndCalculations(locale: SupportedLocales): List<Pair<LoanDomain, LoanCalculation>> = withContext(schedulers.io) {
        loanLocalDataSource
            .getLoans()
            .orEmpty()
            .map { it.toDomain() }
            .map { it to calculate(it, locale) }
    }

    suspend fun saveLoan(loanDomain: LoanDomain) = withContext(schedulers.io) {
        loanLocalDataSource.saveLoan(loanDomain.toCached())
    }

    suspend fun deleteLoanById(loanId: Long) = withContext(schedulers.io) {
        loanLocalDataSource.deleteLoanById(loanId)
    }

    suspend fun getLoanById(loanId: Long): LoanDomain? = withContext(schedulers.io) {
        loanLocalDataSource.getLoanById(loanId)?.toDomain()
    }

    /**
     * Calculates the loan meta data.
     *
     * @param loanDomain the information about the loan
     * @param locale the locale to calculate the mortgage from
     *
     * @return the loan calculation
     */
    suspend fun calculate(
        loanDomain: LoanDomain,
        locale: SupportedLocales
    ): LoanCalculation =
        withContext(schedulers.cpu) {
            return@withContext when (locale) {
                SupportedLocales.PORTUGAL -> portugalLoanCalculator.calculate(loanDomain)
            }
        }

    @Deprecated("Use Room instead. This will be removed in the future, for now it is only maintained for migration purposes.")
    suspend fun getMortgageLoan(): UserMortgageDomain? {
        return loanLocalDataSource.getMortgageLoan()?.toDomain()
    }
}
