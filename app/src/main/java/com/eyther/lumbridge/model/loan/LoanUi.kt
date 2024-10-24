package com.eyther.lumbridge.model.loan

import java.time.LocalDate

data class LoanUi(
    val id: Long = -1,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val loanAmount: Float,
    val loanCategoryUi: LoanCategoryUi,
    val loanInterestRateUi: LoanInterestRateUi
)
