package com.eyther.lumbridge.domain.usecase.expenses

import com.eyther.lumbridge.domain.model.expenses.ExpenseDomain
import com.eyther.lumbridge.domain.repository.expenses.ExpensesRepository
import javax.inject.Inject

class UpdateExpenseUseCase @Inject constructor(
    private val expensesRepository: ExpensesRepository
){
    suspend operator fun invoke(expenseDomain: ExpenseDomain) {
        expensesRepository.saveExpense(expenseDomain)
    }
}
