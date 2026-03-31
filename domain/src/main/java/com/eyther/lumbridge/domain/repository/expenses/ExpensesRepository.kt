package com.eyther.lumbridge.domain.repository.expenses

import com.eyther.lumbridge.domain.mapper.expenses.toCached
import com.eyther.lumbridge.domain.mapper.expenses.toDomain
import com.eyther.lumbridge.domain.model.expenses.ExpenseDomain
import com.eyther.lumbridge.shared.di.model.Schedulers
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ExpensesRepository {
    val expensesFlow
    fun getExpensesByDate(year: Int, month: Int)
    suspend fun saveExpense(expense: ExpenseDomain)
    suspend fun deleteExpenseById(expenseId: Long)
    suspend fun getExpenseById(expenseId: Long)
    suspend fun deleteExpensesByIds(expenseIds: List<Long>)
}
