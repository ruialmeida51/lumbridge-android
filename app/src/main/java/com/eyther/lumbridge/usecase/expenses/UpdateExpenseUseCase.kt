package com.eyther.lumbridge.usecase.expenses

import com.eyther.lumbridge.domain.repository.expenses.ExpensesRepository
import com.eyther.lumbridge.mapper.expenses.toDomain
import com.eyther.lumbridge.model.expenses.ExpenseUi
import javax.inject.Inject

class UpdateExpenseUseCase @Inject constructor(
    private val expensesRepository: ExpensesRepository
){
    suspend operator fun invoke(expenseUi: ExpenseUi) {
        expensesRepository.saveExpense(expenseUi.toDomain())
    }
}
