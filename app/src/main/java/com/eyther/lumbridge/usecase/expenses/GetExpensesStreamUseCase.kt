package com.eyther.lumbridge.usecase.expenses

import com.eyther.lumbridge.domain.repository.expenses.ExpensesRepository
import com.eyther.lumbridge.mapper.expenses.toUi
import com.eyther.lumbridge.model.expenses.ExpenseUi
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
