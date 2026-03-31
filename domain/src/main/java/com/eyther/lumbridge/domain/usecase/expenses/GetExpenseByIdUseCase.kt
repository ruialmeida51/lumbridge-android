package com.eyther.lumbridge.domain.usecase.expenses

import com.eyther.lumbridge.domain.repository.expenses.ExpensesRepository
import com.eyther.lumbridge.domain.mapper.expenses.toUi
import com.eyther.lumbridge.domain.model.expenses.ExpenseUi
import javax.inject.Inject

class GetExpenseByIdUseCase @Inject constructor(
    private val expensesRepository: ExpensesRepository
) {
    suspend operator fun invoke(expenseId: Long): ExpenseUi? {
        return expensesRepository.getExpenseById(expenseId)?.toUi()
    }
}
