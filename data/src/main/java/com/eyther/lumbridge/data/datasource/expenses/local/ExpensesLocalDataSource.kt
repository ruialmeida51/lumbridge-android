package com.eyther.lumbridge.data.datasource.expenses.local

import com.eyther.lumbridge.data.datasource.expenses.dao.ExpensesDao
import com.eyther.lumbridge.data.mappers.expenses.toCached
import com.eyther.lumbridge.data.mappers.expenses.toEntity
import com.eyther.lumbridge.data.model.expenses.local.ExpenseCached
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class ExpensesLocalDataSource @Inject constructor(
    private val expensesDao: ExpensesDao
) {
    val expensesFlow: Flow<List<ExpenseCached>> = expensesDao.getAllExpenses()
        .mapNotNull { flowItem ->
            flowItem?.map { expenseEntity -> expenseEntity.toCached() }
        }

    fun getExpensesByDate(year: Int, month: Int): Flow<List<ExpenseCached>> {
        // Prefix month with 0 if it's a single digit
        val monthString = month.toString().padStart(
            length = 2,
            padChar = '0'
        )

        val yearString = year.toString()

        return expensesDao.getExpensesByDate(yearString, monthString)
            .mapNotNull { flowItem ->
                flowItem?.map { expenseEntity -> expenseEntity.toCached() }
            }
    }

    suspend fun saveExpense(expenseCached: ExpenseCached) {
        if (expenseCached.expenseId == -1L) {
            expensesDao.saveExpense(expenseCached.toEntity())
        } else {
            expensesDao.updateExpense(expenseCached.toEntity().copy(expenseId = expenseCached.expenseId))
        }
    }

    suspend fun getExpenseById(expenseId: Long): ExpenseCached? {
        return expensesDao.getExpenseById(expenseId)?.toCached()
    }

    suspend fun deleteExpenseById(expenseId: Long) {
        expensesDao.deleteExpenseById(expenseId)
    }

    suspend fun deleteExpensesByIds(expenseIds: List<Long>) {
        expensesDao.deleteExpensesByIds(expenseIds)
    }
}
