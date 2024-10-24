package com.eyther.lumbridge.data.datasource.loan.local

import com.eyther.lumbridge.data.datasource.loan.dao.LoanDao
import com.eyther.lumbridge.data.mappers.loan.toCached
import com.eyther.lumbridge.data.mappers.loan.toEntity
import com.eyther.lumbridge.data.model.loan.local.LoanCached
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoanLocalDataSource @Inject constructor(
    private val loanDao: LoanDao
) {
    val loansFlow = loanDao
        .getAllLoans()
        .map { flowItem -> flowItem?.map { loanEntity -> loanEntity.toCached() } }

    suspend fun saveLoan(loan: LoanCached) {
        if (loan.id == -1L) {
            loanDao.insertLoan(loan.toEntity())
        } else {
            loanDao.updateLoan(loan.toEntity().copy(loanId = loan.id))
        }
    }

    suspend fun deleteLoanById(loanId: Long) {
        loanDao.deleteLoanById(loanId)
    }

    suspend fun getLoanById(loanId: Long): LoanCached? {
        return loanDao.getLoanById(loanId)?.toCached()
    }

}
