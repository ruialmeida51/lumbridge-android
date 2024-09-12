package com.eyther.lumbridge.domain.repository.expenses

import com.eyther.lumbridge.data.datasource.expenses.local.ExpensesLocalDataSource
import com.eyther.lumbridge.domain.mapper.expenses.toCached
import com.eyther.lumbridge.domain.mapper.expenses.toDomain
import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryDomain
import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryTypes
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

    suspend fun getMonthOfExpensesByYearMonth(year: Int, month: Int): ExpensesMonthDomain? = withContext(Dispatchers.IO) {
        expensesLocalDataSource.getMonthOfExpensesByYearMonth(year, month)?.toDomain()
    }

    suspend fun getAllCategoriesOfMonthById(categoryId: Long): List<ExpensesCategoryDomain> = withContext(Dispatchers.IO) {
        expensesLocalDataSource.getAllCategoriesOfMonthById(categoryId).map { it.toDomain() }
    }

    suspend fun getCategoryById(categoryId: Long): ExpensesCategoryDomain = withContext(Dispatchers.IO) {
        expensesLocalDataSource.getCategoryById(categoryId).toDomain()
    }

    suspend fun getDetailedExpenseById(detailedExpenseId: Long): ExpensesDetailedDomain = withContext(Dispatchers.IO) {
        expensesLocalDataSource.getDetailedExpenseById(detailedExpenseId).toDomain()
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

    suspend fun updateExpensesDetail(
        expensesDetailed: ExpensesDetailedDomain,
        categoryType: ExpensesCategoryTypes
    ) = withContext(Dispatchers.IO) {
        // Get the old category information
        val cachedCategory = expensesLocalDataSource.getCategoryById(expensesDetailed.parentCategoryId)

        if (cachedCategory.categoryTypeOrdinal != categoryType.ordinal) {
            // Create the new category if the ordinal of the new category is different from the old category
            val newCategory = ExpensesCategoryDomain(
                parentMonthId = cachedCategory.parentMonthId,
                categoryType = categoryType,
                detailedExpenses = listOf(expensesDetailed)
            )

            // Save the new category
            val categoryId = expensesLocalDataSource.saveCategory(newCategory.toCached())

            // Update the detailed expense with the new category ID
            expensesLocalDataSource.updateExpensesDetail(
                expensesDetailedCached = expensesDetailed.toCached().copy(
                    parentCategoryId = categoryId
                )
            )

            // If the old category has only one detail expense, delete the old category as
            // it was replaced by the new category.
            if (cachedCategory.details.size <= 1) {
                expensesLocalDataSource.deleteExpenseCategory(cachedCategory.id)
            }
        } else {
            // If the category type is the same as the old category, just update the detailed expense
            expensesLocalDataSource.updateExpensesDetail(expensesDetailed.toCached())
        }
    }

    suspend fun deleteExpense(expensesMonthDomain: ExpensesMonthDomain) = withContext(Dispatchers.IO) {
        expensesLocalDataSource.deleteExpense(expensesMonthDomain.toCached())
    }

    suspend fun deleteExpenseDetailed(detailedExpenseId: Long) = withContext(Dispatchers.IO) {
        expensesLocalDataSource.deleteExpenseDetailed(detailedExpenseId)
    }
}