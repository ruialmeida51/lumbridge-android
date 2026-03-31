package com.eyther.lumbridge.domain.usecase.expenses

import com.eyther.lumbridge.domain.repository.expenses.ExpensesRepository
import com.eyther.lumbridge.domain.mapper.expenses.toDomain
import com.eyther.lumbridge.domain.model.expenses.ExpenseUi
import javax.inject.Inject

class UpdateExpenseUseCase @Inject constructor(
    private val expensesRepository: ExpensesRepository
){
    suspend operator fun invoke(expenseUi: ExpenseUi) {
        expensesRepository.saveExpense(expenseUi.toDomain())
    }
}
