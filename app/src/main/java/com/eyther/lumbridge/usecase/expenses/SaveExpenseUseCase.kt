package com.eyther.lumbridge.usecase.expenses

import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryTypes
import com.eyther.lumbridge.domain.repository.expenses.ExpensesRepository
import com.eyther.lumbridge.mapper.expenses.toDomain
import com.eyther.lumbridge.model.expenses.ExpensesCategoryUi
import com.eyther.lumbridge.model.expenses.ExpensesDetailedUi
import com.eyther.lumbridge.model.expenses.ExpensesMonthUi
import com.eyther.lumbridge.usecase.finance.GetNetSalaryUseCase
import com.eyther.lumbridge.usecase.user.financials.GetUserFinancials
import java.time.Month
import java.time.Year
import javax.inject.Inject

class SaveExpenseUseCase @Inject constructor(
    private val expensesRepository: ExpensesRepository,
    private val getNetSalaryUseCase: GetNetSalaryUseCase,
    private val userFinancials: GetUserFinancials
) {
    /**
     * TODO Improvements: Maybe some of this logic belongs in the repository.
     *  All the object creation part, perhaps.
     */
    suspend operator fun invoke(
        month: Month,
        year: Year,
        day: Int,
        name: String,
        amount: Float,
        type: ExpensesCategoryTypes
    ) {
        // Check if the year-month combination already exists
        val monthExpense = expensesRepository.getMonthOfExpensesByYearMonth(year.value, month.value)

        // If it does, update the existing month
        if (monthExpense != null) {
            // First, check if the category already exists
            val categories = expensesRepository.getAllCategoriesOfMonthById(monthExpense.id)
            val category = categories.find { it.categoryType == type }

            if (category != null) {
                // If it does, update the existing category
                val detailedExpense = ExpensesDetailedUi(
                    expenseName = name,
                    expenseAmount = amount
                )

                val categoryWithExpenses = category.copy(
                    detailedExpenses = category.detailedExpenses + detailedExpense.toDomain()
                )

                val monthWithUpdatedCategory = monthExpense.copy(
                    categoryExpenses = monthExpense.categoryExpenses.map {
                        if (it.id == category.id) categoryWithExpenses else it
                    }
                )

                expensesRepository.saveNewExpenseOnExistingMonth(
                    expenses = monthWithUpdatedCategory,
                    expensesCategoryDomain = categoryWithExpenses,
                    expensesDetailedDomain = detailedExpense.toDomain()
                )
            } else {
                // If it doesn't, create a new category
                val newDetailedExpense = ExpensesDetailedUi(
                    expenseName = name,
                    expenseAmount = amount
                )

                val newCategory = ExpensesCategoryUi(
                    categoryType = type,
                    expensesDetailedUi = listOf(newDetailedExpense)
                )

                val monthWithNewCategory = monthExpense.copy(
                    categoryExpenses = monthExpense.categoryExpenses + newCategory.toDomain()
                )

                expensesRepository.saveNewExpenseOnExistingMonth(
                    expenses = monthWithNewCategory,
                    expensesCategoryDomain = newCategory.toDomain(),
                    expensesDetailedDomain = newDetailedExpense.toDomain()
                )
            }
        } else {
            // If it doesn't, create a new month

            val userFinancials = userFinancials()
            val snapshotMonthlySalary = userFinancials?.let { getNetSalaryUseCase(it).monthlyNetSalary } ?: 0f

            val newMonth = ExpensesMonthUi(
                month = month,
                year = year,
                day = day,
                snapshotMonthlyNetSalary = snapshotMonthlySalary,
                categoryExpenses = listOf(
                    ExpensesCategoryUi(
                        categoryType = type,
                        expensesDetailedUi = listOf(
                            ExpensesDetailedUi(
                                expenseName = name,
                                expenseAmount = amount
                            )
                        )
                    )
                )
            )

            expensesRepository.saveNewExpense(newMonth.toDomain())
        }
    }
}
