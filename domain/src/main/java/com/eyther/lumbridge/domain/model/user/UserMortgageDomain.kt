package com.eyther.lumbridge.domain.model.user

import com.eyther.lumbridge.domain.model.finance.mortgage.MortgageType
import com.eyther.lumbridge.domain.time.monthsUntil
import java.time.LocalDate

data class UserMortgageDomain(
    val mortgageType: MortgageType,
    val loanAmount: Float,
    val euribor: Float?,
    val spread: Float?,
    val fixedInterestRate: Float?,
    val startDate: LocalDate,
    val endDate: LocalDate
) {
    val monthsRemaining: Int
        get() = startDate.monthsUntil(endDate)

    val interestRate: Float
        get() = when (mortgageType) {
            MortgageType.FIXED -> checkNotNull(fixedInterestRate)
            MortgageType.VARIABLE -> checkNotNull(euribor) + checkNotNull(spread)
        }
}
