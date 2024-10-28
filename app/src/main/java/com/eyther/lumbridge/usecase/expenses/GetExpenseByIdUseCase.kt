package com.eyther.lumbridge.usecase.expenses

import com.eyther.lumbridge.domain.repository.expenses.ExpensesRepository
import com.eyther.lumbridge.mapper.expenses.toUi
import com.eyther.lumbridge.model.expenses.ExpenseUi
import javax.inject.Inject

class GetExpenseByIdUseCase @Inject constructor(
    private val expensesRepository: ExpensesRepository
) {
    suspend operator fun invoke(expenseId: Long): ExpenseUi? {
        return expensesRepository.getExpenseById(expenseId)?.toUi()
    }
}
