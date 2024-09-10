package com.eyther.lumbridge.usecase.expenses

import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryTypes
import com.eyther.lumbridge.domain.repository.expenses.ExpensesRepository
import com.eyther.lumbridge.mapper.expenses.toUi
import javax.inject.Inject

class GetExpenseCategoryTypeUseCase @Inject constructor(
    private val expensesRepository: ExpensesRepository
) {
    suspend operator fun invoke(categoryId: Long): ExpensesCategoryTypes {
        return expensesRepository.getCategoryById(categoryId).toUi().categoryType
    }
}
