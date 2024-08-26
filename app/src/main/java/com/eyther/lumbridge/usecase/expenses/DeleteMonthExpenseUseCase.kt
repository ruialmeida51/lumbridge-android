package com.eyther.lumbridge.usecase.expenses

import com.eyther.lumbridge.domain.repository.expenses.ExpensesRepository
import com.eyther.lumbridge.mapper.expenses.toDomain
import com.eyther.lumbridge.model.expenses.ExpensesMonthUi
import javax.inject.Inject

class DeleteMonthExpenseUseCase @Inject constructor(
    private val expensesRepository: ExpensesRepository
) {
    suspend operator fun invoke(expensesMonthUi: ExpensesMonthUi) {
        expensesRepository.deleteExpense(expensesMonthUi.toDomain())
    }
}
