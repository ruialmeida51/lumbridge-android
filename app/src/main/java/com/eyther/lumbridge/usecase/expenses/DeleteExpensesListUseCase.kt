package com.eyther.lumbridge.usecase.expenses

import com.eyther.lumbridge.domain.repository.expenses.ExpensesRepository
import javax.inject.Inject

class DeleteExpensesListUseCase @Inject constructor(
    private val expensesRepository: ExpensesRepository
) {
    suspend operator fun invoke(expenseId: List<Long>) {
        expensesRepository.deleteExpensesByIds(expenseId)
    }
}
