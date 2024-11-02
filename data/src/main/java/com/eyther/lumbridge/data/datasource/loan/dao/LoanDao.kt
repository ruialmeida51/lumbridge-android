package com.eyther.lumbridge.data.datasource.loan.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.eyther.lumbridge.data.model.loan.entity.LOAN_TABLE_NAME
import com.eyther.lumbridge.data.model.loan.entity.LoanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LoanDao {

    @Transaction
    @Query("SELECT * FROM $LOAN_TABLE_NAME")
    fun getAllLoansFlow(): Flow<List<LoanEntity>?>

    @Transaction
    @Query("SELECT * FROM $LOAN_TABLE_NAME WHERE loanId = :loanId")
    fun getLoanByIdStream(loanId: Long): Flow<LoanEntity?>

    @Transaction
    @Query("SELECT * FROM $LOAN_TABLE_NAME")
    suspend fun getAllLoans(): List<LoanEntity>?

    @Transaction
    @Query("SELECT * FROM $LOAN_TABLE_NAME WHERE loanId = :loanId")
    suspend fun getLoanById(loanId: Long): LoanEntity?

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoan(loan: LoanEntity): Long

    @Transaction
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateLoan(loan: LoanEntity)

    @Transaction
    @Query("DELETE FROM $LOAN_TABLE_NAME WHERE loanId = :loanId")
    suspend fun deleteLoanById(loanId: Long)
}
