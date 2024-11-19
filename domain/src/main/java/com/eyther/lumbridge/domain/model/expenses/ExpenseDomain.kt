package com.eyther.lumbridge.domain.model.expenses

import com.eyther.lumbridge.domain.model.netsalary.allocation.MoneyAllocationType
import java.time.LocalDate

data class ExpenseDomain(
    val id: Long,
    val categoryType: ExpensesCategoryTypes,
    val allocation: MoneyAllocationType,
    val expenseName: String,
    val expenseAmount: Float,
    val date: LocalDate
)
