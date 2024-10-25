package com.eyther.lumbridge.data.model.user.local

@Deprecated("Use Room instead. This will be removed in the future, for now it is only maintained for migration purposes.")
data class UserMortgageCached(
    val loanAmount: Float,
    val euribor: Float?,
    val spread: Float?,
    val fixedInterestRate: Float?,
    val startDate: String,
    val endDate: String
)
