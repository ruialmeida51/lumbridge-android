package com.eyther.lumbridge.domain.usecase.expenses

import com.eyther.lumbridge.domain.model.expenses.ExpenseDomain
import com.eyther.lumbridge.domain.repository.expenses.ExpensesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExpensesStreamByDateUseCase @Inject constructor(
    private val expensesRepository: ExpensesRepository
) {
    operator fun invoke(year: Int, month: Int): Flow<List<ExpenseDomain>> {
        return expensesRepository.getExpensesByDate(year, month)
    }
}
