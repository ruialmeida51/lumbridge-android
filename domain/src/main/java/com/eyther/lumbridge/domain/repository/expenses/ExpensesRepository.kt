package com.eyther.lumbridge.domain.repository.expenses

import com.eyther.lumbridge.data.datasource.expenses.local.ExpensesLocalDataSource
import com.eyther.lumbridge.domain.mapper.expenses.toCached
import com.eyther.lumbridge.domain.mapper.expenses.toDomain
import com.eyther.lumbridge.domain.model.expenses.ExpenseDomain
import com.eyther.lumbridge.shared.di.model.Schedulers
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExpensesRepository @Inject constructor(
    private val expensesLocalDataSource: ExpensesLocalDataSource,
    private val schedulers: Schedulers
) {
    val expensesFlow = expensesLocalDataSource
        .expensesFlow
        .mapNotNull { it.toDomain() }

    fun getExpensesByDate(year: Int, month: Int) = expensesLocalDataSource
        .getExpensesByDate(year, month)
        .mapNotNull { it.toDomain() }

    suspend fun saveExpense(expense: ExpenseDomain) = withContext(schedulers.io) {
        expensesLocalDataSource.saveExpense(expense.toCached())
    }

    suspend fun deleteExpenseById(expenseId: Long) = withContext(schedulers.io) {
        expensesLocalDataSource.deleteExpenseById(expenseId)
    }

    suspend fun getExpenseById(expenseId: Long) = withContext(schedulers.io) {
        expensesLocalDataSource.getExpenseById(expenseId)?.toDomain()
    }

    suspend fun deleteExpensesByIds(expenseIds: List<Long>) = withContext(schedulers.io) {
        expensesLocalDataSource.deleteExpensesByIds(expenseIds)
    }
}
