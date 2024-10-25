package com.eyther.lumbridge.domain.model.user

import java.time.LocalDate

@Deprecated("The user mortgage domain has been replaced by the loan domain, moved into Room. Please, use the new domain instead of this one.")
data class UserMortgageDomain(
    val loanAmount: Float,
    val euribor: Float?,
    val spread: Float?,
    val fixedInterestRate: Float?,
    val startDate: LocalDate,
    val endDate: LocalDate
)
