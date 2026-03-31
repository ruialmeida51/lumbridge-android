package com.eyther.lumbridge.domain.usecase.expenses

import com.eyther.lumbridge.domain.repository.expenses.ExpensesRepository
import com.eyther.lumbridge.domain.mapper.expenses.toUi
import com.eyther.lumbridge.domain.model.expenses.ExpenseUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetExpensesStreamUseCase @Inject constructor(
    private val expensesRepository: ExpensesRepository
) {
    operator fun invoke(): Flow<List<ExpenseUi>> {
        return expensesRepository.expensesFlow
            .map { expensesList ->
                expensesList
                    .map { expense -> expense.toUi() }
            }
    }
}
