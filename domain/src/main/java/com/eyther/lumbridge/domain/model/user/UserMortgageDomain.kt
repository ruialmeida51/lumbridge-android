package com.eyther.lumbridge.domain.model.user

import com.eyther.lumbridge.domain.model.finance.MortgageType

data class UserMortgageDomain(
    val mortgageType: MortgageType,
    val loanAmount: Float,
    val monthsLeft: Int,
    val euribor: Float?,
    val spread: Float?,
    val fixedInterestRate: Float?
)
