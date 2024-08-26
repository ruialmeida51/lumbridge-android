package com.eyther.lumbridge.usecase.expenses

import com.eyther.lumbridge.domain.repository.expenses.ExpensesRepository
import javax.inject.Inject

class DeleteDetailedExpenseUseCase @Inject constructor(
    private val expensesRepository: ExpensesRepository
) {
    suspend operator fun invoke(detailedExpenseId: Long) {
        expensesRepository.deleteExpenseDetailed(detailedExpenseId)
    }
}
