package com.eyther.lumbridge.data.repository.loan

import com.eyther.lumbridge.data.datasource.loan.local.LoanLocalDataSource
import com.eyther.lumbridge.data.mapper.loan.toCached
import com.eyther.lumbridge.data.mapper.loan.toDomain
import com.eyther.lumbridge.data.mapper.user.toDomain
import com.eyther.lumbridge.data.repository.loan.portugal.PortugalLoanCalculator
import com.eyther.lumbridge.domain.model.loan.LoanDomain
import com.eyther.lumbridge.domain.model.loan.LoanCalculation
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.domain.model.user.UserMortgageDomain
import com.eyther.lumbridge.domain.repository.loan.LoanRepository
import com.eyther.lumbridge.shared.di.model.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoanRepositoryImpl @Inject constructor(
    private val portugalLoanCalculator: PortugalLoanCalculator,
    private val loanLocalDataSource: LoanLocalDataSource,
    private val schedulers: Schedulers
) : LoanRepository {

    override fun getLoansAndCalculationsFlow(locale: SupportedLocales): Flow<List<Pair<LoanDomain, LoanCalculation>>> =
        loanLocalDataSource
            .loansFlow
            .map { cachedLoans ->
                cachedLoans
                    .toDomain()
                    .map { it to calculate(it, locale) }
            }

    override fun getLoanAndCalculationByIdStream(
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

    override suspend fun getLoansAndCalculations(locale: SupportedLocales): List<Pair<LoanDomain, LoanCalculation>> = withContext(schedulers.io) {
        loanLocalDataSource
            .getLoans()
            .map { it.toDomain() }
            .map { it to calculate(it, locale) }
    }

    override suspend fun saveLoan(loanDomain: LoanDomain) = withContext(schedulers.io) {
        loanLocalDataSource.saveLoan(loanDomain.toCached())
    }

    override suspend fun deleteLoanById(loanId: Long) = withContext(schedulers.io) {
        loanLocalDataSource.deleteLoanById(loanId)
    }

    override suspend fun getLoanById(loanId: Long): LoanDomain? = withContext(schedulers.io) {
        loanLocalDataSource.getLoanById(loanId)?.toDomain()
    }

    override suspend fun calculate(
        loanDomain: LoanDomain,
        locale: SupportedLocales
    ): LoanCalculation = withContext(schedulers.cpu) {
        return@withContext when (locale) {
            SupportedLocales.PORTUGAL -> portugalLoanCalculator.calculate(loanDomain)
        }
    }

    @Deprecated("Use Room instead. This will be removed in the future, for now it is only maintained for migration purposes.")
    override suspend fun getMortgageLoan(): UserMortgageDomain? {
        return loanLocalDataSource.getMortgageLoan()?.toDomain()
    }
}
