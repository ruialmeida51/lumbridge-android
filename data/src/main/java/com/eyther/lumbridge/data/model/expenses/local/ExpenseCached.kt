package com.eyther.lumbridge.data.model.expenses.local

import java.time.LocalDate

data class ExpenseCached(
    val expenseId: Long = -1,
    val categoryTypeOrdinal: Int,
    val date: LocalDate,
    val amount: Float,
    val name: String
)
