package com.eyther.lumbridge.data.model.user

data class UserMortgageCached(
    val mortgageType: String,
    val loanAmount: Float,
    val euribor: Float?,
    val spread: Float?,
    val fixedInterestRate: Float?,
    val startDate: String,
    val endDate: String
)
