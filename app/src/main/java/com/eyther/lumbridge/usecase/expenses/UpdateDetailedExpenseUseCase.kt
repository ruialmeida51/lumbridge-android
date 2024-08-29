package com.eyther.lumbridge.usecase.expenses

import com.eyther.lumbridge.domain.repository.expenses.ExpensesRepository
import com.eyther.lumbridge.mapper.expenses.toDomain
import com.eyther.lumbridge.model.expenses.ExpensesDetailedUi
import javax.inject.Inject

class UpdateDetailedExpenseUseCase @Inject constructor(
    private val expensesRepository: ExpensesRepository
){
    suspend operator fun invoke(
        expensesDetailedUi: ExpensesDetailedUi
    ) {
        expensesRepository.updateExpensesDetail(expensesDetailedUi.toDomain())
    }
}
