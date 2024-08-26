package com.eyther.lumbridge.usecase.expenses

import com.eyther.lumbridge.domain.repository.expenses.ExpensesRepository
import com.eyther.lumbridge.mapper.expenses.toUi
import com.eyther.lumbridge.model.expenses.ExpensesDetailedUi
import javax.inject.Inject

class GetDetailedExpenseUseCase @Inject constructor(
    private val expensesRepository: ExpensesRepository
) {
    suspend operator fun invoke(detailedExpenseId: Long): ExpensesDetailedUi {
        return expensesRepository.getDetailedExpense(detailedExpenseId).toUi()
    }
}
