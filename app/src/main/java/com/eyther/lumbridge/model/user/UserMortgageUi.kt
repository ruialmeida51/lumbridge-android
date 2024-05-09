package com.eyther.lumbridge.model.user

import com.eyther.lumbridge.model.mortgage.MortgageTypeUi

data class UserMortgageUi(
    val mortgageType: MortgageTypeUi,
    val loanAmount: Float,
    val monthsLeft: Int,
    val euribor: Float?,
    val spread: Float?,
    val fixedInterestRate: Float?
)
