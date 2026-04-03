package com.eyther.lumbridge.usecase.expenses

import com.eyther.lumbridge.domain.model.expenses.ExpenseDomain
import com.eyther.lumbridge.domain.repository.expenses.ExpensesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExpensesStreamUseCase @Inject constructor(
    private val expensesRepository: ExpensesRepository
) {
    operator fun invoke(): Flow<List<ExpenseDomain>> {
        return expensesRepository.expensesFlow
    }
}
