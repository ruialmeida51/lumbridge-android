package com.eyther.lumbridge.domain.repository.loan

import com.eyther.lumbridge.data.datasource.loan.local.LoanLocalDataSource
import com.eyther.lumbridge.domain.mapper.loan.toCached
import com.eyther.lumbridge.domain.mapper.loan.toDomain
import com.eyther.lumbridge.domain.model.loan.Loan
import com.eyther.lumbridge.domain.model.loan.LoanCalculation
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
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
    fun getLoansFlow(locale: SupportedLocales): Flow<List<Pair<Loan, LoanCalculation>>> =
        loanLocalDataSource
            .loansFlow
            .mapNotNull { cachedLoans ->
                cachedLoans
                    ?.toDomain()
                    ?.map { it to calculate(it, locale) }
            }

    suspend fun saveLoan(loan: Loan) = withContext(schedulers.io) {
        loanLocalDataSource.saveLoan(loan.toCached())
    }

    suspend fun deleteLoanById(loanId: Long) = withContext(schedulers.io) {
        loanLocalDataSource.deleteLoanById(loanId)
    }

    suspend fun getLoanById(loanId: Long): Loan? = withContext(schedulers.io) {
        loanLocalDataSource.getLoanById(loanId)?.toDomain()
    }

    /**
     * Calculates the loan meta data.
     *
     * @param loan the information about the loan
     * @param locale the locale to calculate the mortgage from
     *
     * @return the loan calculation
     */
    suspend fun calculate(
        loan: Loan,
        locale: SupportedLocales
    ): LoanCalculation =
        withContext(schedulers.cpu) {
            return@withContext when (locale) {
                SupportedLocales.PORTUGAL -> portugalLoanCalculator.calculate(loan)
            }
        }
}
