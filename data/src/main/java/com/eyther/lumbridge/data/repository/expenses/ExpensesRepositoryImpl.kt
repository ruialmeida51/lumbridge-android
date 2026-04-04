package com.eyther.lumbridge.data.repository.expenses

import com.eyther.lumbridge.data.datasource.expenses.local.ExpensesLocalDataSource
import com.eyther.lumbridge.data.mapper.expenses.toCached
import com.eyther.lumbridge.data.mapper.expenses.toDomain
import com.eyther.lumbridge.domain.model.expenses.ExpenseDomain
import com.eyther.lumbridge.domain.repository.expenses.ExpensesRepository
import com.eyther.lumbridge.shared.di.model.Schedulers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExpensesRepositoryImpl @Inject constructor(
    private val expensesLocalDataSource: ExpensesLocalDataSource,
    private val schedulers: Schedulers
) : ExpensesRepository {

    override val expensesFlow = expensesLocalDataSource
        .expensesFlow
        .map { it.toDomain() }

    override fun getExpensesByDate(year: Int, month: Int) = expensesLocalDataSource
        .getExpensesByDate(year, month)
        .map { it.toDomain() }

    override suspend fun saveExpense(expense: ExpenseDomain) = withContext(schedulers.io) {
        expensesLocalDataSource.saveExpense(expense.toCached())
    }

    override suspend fun deleteExpenseById(expenseId: Long) = withContext(schedulers.io) {
        expensesLocalDataSource.deleteExpenseById(expenseId)
    }

    override suspend fun getExpenseById(expenseId: Long) = withContext(schedulers.io) {
        expensesLocalDataSource.getExpenseById(expenseId)?.toDomain()
    }

    override suspend fun deleteExpensesByIds(expenseIds: List<Long>) = withContext(schedulers.io) {
        expensesLocalDataSource.deleteExpensesByIds(expenseIds)
    }
}
