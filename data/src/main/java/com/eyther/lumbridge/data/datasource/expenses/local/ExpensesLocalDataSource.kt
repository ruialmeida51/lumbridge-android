package com.eyther.lumbridge.data.datasource.expenses.local

import com.eyther.lumbridge.data.datasource.expenses.dao.ExpensesDao
import com.eyther.lumbridge.data.mappers.expenses.toCached
import com.eyther.lumbridge.data.mappers.expenses.toEntity
import com.eyther.lumbridge.data.model.expenses.local.ExpensesCategoryCached
import com.eyther.lumbridge.data.model.expenses.local.ExpensesDetailedCached
import com.eyther.lumbridge.data.model.expenses.local.ExpensesMonthCached
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class ExpensesLocalDataSource @Inject constructor(
    private val expensesDao: ExpensesDao
) {
    val expensesFlow: Flow<List<ExpensesMonthCached>> = expensesDao.getAllExpensesForMonth()
        .mapNotNull { flowItem ->
            flowItem?.map { expenseEntity -> expenseEntity.toCached() }
        }

    /**
     * Fetches the detailed expense with the given ID from the local data source.
     *
     * @param detailedExpenseId The ID of the detailed expense to fetch.
     */
    suspend fun getDetailedExpenseById(detailedExpenseId: Long): ExpensesDetailedCached {
        val detailedExpense = expensesDao.getExpensesDetailedById(detailedExpenseId)
            ?: throw IllegalArgumentException("No detailed expense found with ID $detailedExpenseId")

        return detailedExpense.toCached()
    }

    /**
     * Fetches the category with the given ID from the local data source.
     *
     * @param categoryId The ID of the category to fetch.
     */
    suspend fun getCategoryById(categoryId: Long): ExpensesCategoryCached {
        val category = expensesDao.getExpensesCategoryById(categoryId)
            ?: throw IllegalArgumentException("No category found with ID $categoryId")

        return category.expensesCategoryEntity.toCached(category.detailedExpenses)
    }

    /**
     * Fetches the expenses for the given year and month from the local data source.
     *
     * @param year The year to fetch expenses for.
     * @param month The month to fetch expenses for.
     */
    suspend fun getMonthOfExpensesByYearMonth(year: Int, month: Int): ExpensesMonthCached? {
        return expensesDao.getMonthOfExpensesByYearMonth(year, month)?.toCached()
    }

    /**
     * Fetches all categories for the given month from the local data source.
     *
     * @param parentMonthId The ID of the month to fetch categories for.
     */
    suspend fun getAllCategoriesOfMonthById(parentMonthId: Long): List<ExpensesCategoryCached> {
        return runCatching { expensesDao.getAllCategoriesOfMonthById(parentMonthId) }
            .getOrNull()
            .orEmpty()
            .map { it.expensesCategoryEntity.toCached(it.detailedExpenses) }
    }

    /**
     * Saves the given expense to the local data source.
     *
     * This method will run a loop for each month, category, and detail in the given list of expenses.
     *
     * @param expensesMonthCached The expense to save.
     */
    suspend fun saveExpense(expensesMonthCached: ExpensesMonthCached) {
        val monthId = expensesDao.saveExpensesMonth(expensesMonth = expensesMonthCached.toEntity())

        expensesMonthCached.categories.forEach { category ->
            val categoryId = expensesDao.saveExpensesCategory(expensesCategory = category.toEntity(monthId))

            category.details.forEach { detail ->
                expensesDao.saveExpensesDetail(expensesDetailed = detail.toEntity(categoryId))
            }
        }
    }

    /**
     * Saves the given category to the local data source.
     *
     * @return The ID of the saved category.
     */
    suspend fun saveCategory(category: ExpensesCategoryCached): Long {
        return expensesDao.saveExpensesCategory(
            expensesCategory = category.toEntity(category.parentMonthId)
        )
    }

    /**
     * Updates the given expenses in the local data source.
     *
     * This assumes that the given expenses month already exist in the local data source.
     *
     * @param expensesMonthCached The expenses to update.
     */
    suspend fun saveNewExpenseOnExistingMonth(
        expensesMonthCached: ExpensesMonthCached,
        expensesCategoryCached: ExpensesCategoryCached,
        expensesDetailedCached: ExpensesDetailedCached
    ) {
        val categoryEntity = runCatching { expensesDao.getExpensesCategoryById(expensesCategoryCached.id)?.expensesCategoryEntity }.getOrNull()

        if (categoryEntity == null) {
            val newCategory = expensesCategoryCached.toEntity(expensesMonthCached.id)

            val categoryId = expensesDao.saveExpensesCategory(newCategory)

            val detailedEntity = expensesDetailedCached
                .toEntity(categoryId)

            expensesDao.saveExpensesDetail(detailedEntity)
        } else {
            val detailedEntity = expensesDetailedCached
                .toEntity(categoryEntity.categoryId)

            expensesDao.saveExpensesDetail(detailedEntity)
        }
    }

    /**
     * Saves the given detailed expense to the local data source.
     *
     * @param expensesDetailedCached The detailed expense to save
     */
    suspend fun updateExpensesDetail(expensesDetailedCached: ExpensesDetailedCached) {
        val detailEntity = expensesDetailedCached
            .toEntity(expensesDetailedCached.parentCategoryId)
            .copy(detailId = expensesDetailedCached.id)

        expensesDao.updateExpensesDetail(detailEntity)
    }

    /**
     * Deletes the given expense from the local data source.
     *
     * @param expensesMonthCached The expense to delete.
     */
    suspend fun deleteExpense(expensesMonthCached: ExpensesMonthCached) {
        val entity = expensesMonthCached
            .toEntity()
            .copy(monthId = expensesMonthCached.id)

        expensesDao.deleteExpensesMonth(entity)
    }

    /**
     * Deletes the given detailed expense from the local data source.
     *
     * @param detailedExpenseId The ID of the detailed expense to delete.
     */
    suspend fun deleteExpenseDetailed(detailedExpenseId: Long) {
        // Fetch the detailed expense to delete
        val detailedExpense = expensesDao.getExpensesDetailedById(detailedExpenseId)
            ?: throw IllegalArgumentException("No detailed expense found with ID $detailedExpenseId")

        // Fetch the parent category for the detail
        val parentCategory = expensesDao.getExpensesCategoryById(detailedExpense.parentCategoryId)?.expensesCategoryEntity

        // Fetch the parent month for the category
        val parentMonth = expensesDao.getExpensesForMonthById(parentCategory?.parentMonthId ?: 0)

        // Delete the detail
        expensesDao.deleteExpensesDetailed(detailedExpense)

        // After deleting the detailed expense, check if the category has any other details.
        // If not, delete the category.
        expensesDao
            .getExpensesForCategory(detailedExpense.parentCategoryId)
            .detailedExpenses
            .ifEmpty { expensesDao.deleteExpensesCategory(parentCategory!!) }

        // After deleting the category, check if the month has any other categories.
        // If not, delete the month.
        expensesDao
            .getExpensesForMonthById(parentCategory?.parentMonthId ?: 0)
            .categories
            .ifEmpty { expensesDao.deleteExpensesMonth(parentMonth.expensesMonthEntity) }
    }

    /**
     * Deletes the given category from the local data source.
     * If the month has no other categories, the month will also be deleted.
     *
     * @param categoryId The ID of the category to delete.
     * @param deleteMonthIfEmpty Whether to delete the month if it has no other categories.
     */
    suspend fun deleteExpenseCategory(
        categoryId: Long,
        deleteMonthIfEmpty: Boolean = true
    ) {
        // Fetch the current cached category
        val category = expensesDao.getExpensesCategoryById(categoryId)?.expensesCategoryEntity

        // Fetch the parent month for the category
        val month = expensesDao.getExpensesForMonthById(category?.parentMonthId ?: 0)

        // Delete the category
        expensesDao.deleteExpensesCategory(category!!)

        if (deleteMonthIfEmpty) {
            // After deleting the category, check if the month has any other categories.
            // If not, delete the month.
            expensesDao
                .getExpensesForMonthById(category.parentMonthId)
                .categories
                .ifEmpty { expensesDao.deleteExpensesMonth(month.expensesMonthEntity) }
        }
    }
}
