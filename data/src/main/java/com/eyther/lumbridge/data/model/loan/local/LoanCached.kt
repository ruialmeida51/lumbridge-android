package com.eyther.lumbridge.data.model.loan.local

data class LoanCached(
    val id: Long = -1,
    val name: String,
    val startDate: String,
    val endDate: String,
    val currentAmount: Float,
    val initialAmount: Float,
    val fixedTaegInterestRate: Float?,
    val variableEuribor: Float?,
    val variableSpread: Float?,
    val fixedTanInterestRate: Float?,
    val loanTypeOrdinal: Int,
    val loanCategoryOrdinal: Int
)
