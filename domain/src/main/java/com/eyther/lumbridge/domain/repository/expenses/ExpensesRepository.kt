package com.eyther.lumbridge.domain.repository.expenses

import com.eyther.lumbridge.data.datasource.expenses.local.ExpensesLocalDataSource
import com.eyther.lumbridge.domain.mapper.expenses.toCached
import com.eyther.lumbridge.domain.mapper.expenses.toDomain
import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryDomain
import com.eyther.lumbridge.domain.model.expenses.ExpensesDetailedDomain
import com.eyther.lumbridge.domain.model.expenses.ExpensesMonthDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExpensesRepository @Inject constructor(
    private val expensesLocalDataSource: ExpensesLocalDataSource
) {
    fun getExpensesFlow() = expensesLocalDataSource.expensesFlow
        .map { expensesList ->
            // Convert the list of cached expenses to a list of domain expenses
            expensesList.map { expense -> expense.toDomain() }
        }

    suspend fun getExpenseByYearMonth(year: Int, month: Int): ExpensesMonthDomain? = withContext(Dispatchers.IO) {
        expensesLocalDataSource.getExpenseByYearMonth(year, month)?.toDomain()
    }

    suspend fun getMonthCategoryExpense(categoryId: Long): List<ExpensesCategoryDomain> = withContext(Dispatchers.IO) {
        expensesLocalDataSource.getMonthCategoriesExpense(categoryId).map { it.toDomain() }
    }

    suspend fun getDetailedExpense(detailedExpenseId: Long): ExpensesDetailedDomain = withContext(Dispatchers.IO) {
        expensesLocalDataSource.getDetailedExpense(detailedExpenseId).toDomain()
    }

    suspend fun saveNewExpense(expenses: ExpensesMonthDomain) = withContext(Dispatchers.IO) {
        expensesLocalDataSource.saveExpense(expenses.toCached())
    }

    suspend fun saveNewExpenseOnExistingMonth(
        expenses: ExpensesMonthDomain,
        expensesCategoryDomain: ExpensesCategoryDomain,
        expensesDetailedDomain: ExpensesDetailedDomain
    ) = withContext(Dispatchers.IO) {
        expensesLocalDataSource.saveNewExpenseOnExistingMonth(
            expensesMonthCached = expenses.toCached(),
            expensesCategoryCached = expensesCategoryDomain.toCached(),
            expensesDetailedCached = expensesDetailedDomain.toCached()
        )
    }

    suspend fun updateExpensesDetail(expensesDetailed: ExpensesDetailedDomain) = withContext(Dispatchers.IO) {
        expensesLocalDataSource.updateExpensesDetail(expensesDetailed.toCached())
    }

    suspend fun deleteExpense(expensesMonthDomain: ExpensesMonthDomain) = withContext(Dispatchers.IO) {
        expensesLocalDataSource.deleteExpense(expensesMonthDomain.toCached())
    }

    suspend fun deleteExpenseDetailed(detailedExpenseId: Long) = withContext(Dispatchers.IO) {
        expensesLocalDataSource.deleteExpenseDetailed(detailedExpenseId)
    }
}
