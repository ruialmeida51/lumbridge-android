package com.eyther.lumbridge.data.datasource.expenses.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.eyther.lumbridge.data.model.expenses.entity.EXPENSES_TABLE_NAME
import com.eyther.lumbridge.data.model.expenses.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpensesDao {
    @Transaction
    @Query("SELECT * FROM $EXPENSES_TABLE_NAME")
    fun getAllExpenses(): Flow<List<ExpenseEntity>?>

    /**
     * We need to use the SQLite strftime function to partition the stored date into year and month. They are stored as strings,
     * so we also need to provide the query parameters as strings. Check [com.eyther.lumbridge.data.database.converters.RoomConverters] for
     * more information on how the date is stored.
     */
    @Transaction
    @Query("SELECT * FROM $EXPENSES_TABLE_NAME WHERE strftime('%Y', date) = :year AND strftime('%m', date) = :month")
    fun getExpensesByDate(year: String, month: String): Flow<List<ExpenseEntity>?>

    @Transaction
    @Query("SELECT * FROM $EXPENSES_TABLE_NAME WHERE expenseId = :expenseId")
    suspend fun getExpenseById(expenseId: Long): ExpenseEntity?

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveExpense(expense: ExpenseEntity): Long

    @Transaction
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateExpense(expense: ExpenseEntity)

    @Transaction
    @Query("DELETE FROM $EXPENSES_TABLE_NAME WHERE expenseId = :expenseId")
    suspend fun deleteExpenseById(expenseId: Long)

    @Transaction
    @Query("DELETE FROM $EXPENSES_TABLE_NAME WHERE expenseId IN (:expenseIds)")
    suspend fun deleteExpensesByIds(expenseIds: List<Long>)
}
