package com.eyther.lumbridge.model.user

import com.eyther.lumbridge.domain.time.monthsUntil
import com.eyther.lumbridge.model.mortgage.MortgageTypeUi
import java.time.LocalDate

data class UserMortgageUi(
    val mortgageType: MortgageTypeUi,
    val loanAmount: Float,
    val euribor: Float?,
    val spread: Float?,
    val fixedInterestRate: Float?,
    val startDate: LocalDate,
    val endDate: LocalDate
) {
    val monthsRemaining: Int
        get() = startDate.monthsUntil(endDate)
}
