package com.eyther.lumbridge.usecase.expenses

import com.eyther.lumbridge.domain.repository.expenses.ExpensesRepository
import com.eyther.lumbridge.mapper.expenses.toUi
import com.eyther.lumbridge.model.expenses.ExpensesMonthUi
import com.eyther.lumbridge.model.finance.NetSalaryUi
import com.eyther.lumbridge.model.user.UserFinancialsUi
import com.eyther.lumbridge.usecase.finance.GetNetSalary
import com.eyther.lumbridge.usecase.user.financials.GetUserFinancialsStream
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetExpensesStreamUseCase @Inject constructor(
    private val expensesRepository: ExpensesRepository,
    private val getNetSalary: GetNetSalary,
    private val getUserFinancialsStream: GetUserFinancialsStream
) {
    private var cachedFinancials: UserFinancialsUi? = null
    private var cachedNetSalary: NetSalaryUi? = null

    suspend operator fun invoke(): Flow<Pair<NetSalaryUi?, List<ExpensesMonthUi>>> {
        return combine(
            expensesRepository.getExpensesFlow(),
            getUserFinancialsStream()
        ) { expensesList, userFinancials ->
            expensesList to userFinancials
        }
            .map { (expensesList, userFinancials) ->
                if (userFinancials != null && userFinancials != cachedFinancials) {
                    cachedFinancials = userFinancials
                    cachedNetSalary = getNetSalary(userFinancials)
                }

                cachedNetSalary to expensesList
                    .map { expense -> expense.toUi(cachedNetSalary) }
            }
    }
}
