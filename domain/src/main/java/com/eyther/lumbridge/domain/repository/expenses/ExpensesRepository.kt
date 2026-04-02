package com.eyther.lumbridge.domain.repository.expenses

import com.eyther.lumbridge.domain.model.expenses.ExpenseDomain
import kotlinx.coroutines.flow.Flow

interface ExpensesRepository {
    val expensesFlow: Flow<List<ExpenseDomain>>
    fun getExpensesByDate(year: Int, month: Int): Flow<List<ExpenseDomain>>
    suspend fun saveExpense(expense: ExpenseDomain)
    suspend fun deleteExpenseById(expenseId: Long)
    suspend fun getExpenseById(expenseId: Long): ExpenseDomain?
    suspend fun deleteExpensesByIds(expenseIds: List<Long>)
}
