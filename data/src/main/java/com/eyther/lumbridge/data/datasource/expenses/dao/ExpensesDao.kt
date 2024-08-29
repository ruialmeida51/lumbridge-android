package com.eyther.lumbridge.data.datasource.expenses.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.eyther.lumbridge.data.model.expenses.entity.EXPENSES_CATEGORY_TABLE_NAME
import com.eyther.lumbridge.data.model.expenses.entity.EXPENSES_DETAILED_TABLE_NAME
import com.eyther.lumbridge.data.model.expenses.entity.EXPENSES_MONTH_TABLE_NAME
import com.eyther.lumbridge.data.model.expenses.entity.ExpensesCategoryEntity
import com.eyther.lumbridge.data.model.expenses.entity.ExpensesDetailedEntity
import com.eyther.lumbridge.data.model.expenses.entity.ExpensesMonthEntity
import com.eyther.lumbridge.data.model.expenses.query.ExpensesForCategoryWithDetails
import com.eyther.lumbridge.data.model.expenses.query.ExpensesForMonthWithCategoryAndDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpensesDao {

    @Transaction
    @Query("SELECT * FROM $EXPENSES_MONTH_TABLE_NAME")
    fun getAllExpensesForMonth(): Flow<List<ExpensesForMonthWithCategoryAndDetails>?>

    @Transaction
    @Query("SELECT * FROM $EXPENSES_CATEGORY_TABLE_NAME WHERE categoryId = :categoryId")
    suspend fun getExpensesForCategory(categoryId: Long): ExpensesForCategoryWithDetails

    @Transaction
    @Query("SELECT * FROM $EXPENSES_CATEGORY_TABLE_NAME WHERE parentMonthId = :parentMonthId")
    suspend fun getAllCategoriesOfMonthById(parentMonthId: Long): List<ExpensesForCategoryWithDetails>

    @Transaction
    @Query("SELECT * FROM $EXPENSES_MONTH_TABLE_NAME WHERE monthId = :monthId")
    suspend fun getExpensesForMonthById(monthId: Long): ExpensesForMonthWithCategoryAndDetails

    @Transaction
    @Query("SELECT * FROM $EXPENSES_MONTH_TABLE_NAME WHERE year = :year AND month = :month")
    suspend fun getMonthOfExpensesByYearMonth(year: Int, month: Int): ExpensesForMonthWithCategoryAndDetails?

    @Transaction
    @Query("SELECT * FROM $EXPENSES_CATEGORY_TABLE_NAME WHERE categoryId = :categoryId")
    suspend fun getExpensesCategoryById(categoryId: Long): ExpensesForCategoryWithDetails?

    @Transaction
    @Query("SELECT * FROM $EXPENSES_DETAILED_TABLE_NAME WHERE detailId = :detailedExpenseId")
    suspend fun getExpensesDetailedById(detailedExpenseId: Long): ExpensesDetailedEntity?

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveExpensesMonth(expensesMonth: ExpensesMonthEntity): Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveExpensesCategory(expensesCategory: ExpensesCategoryEntity): Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveExpensesDetail(expensesDetailed: ExpensesDetailedEntity): Long

    @Transaction
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateExpensesDetail(expensesDetailed: ExpensesDetailedEntity)

    @Transaction
    @Delete
    suspend fun deleteExpensesMonth(expensesMonth: ExpensesMonthEntity)

    @Transaction
    @Delete
    suspend fun deleteExpensesCategory(expensesCategory: ExpensesCategoryEntity)

    @Transaction
    @Delete
    suspend fun deleteExpensesDetailed(expensesDetailed: ExpensesDetailedEntity)
}
