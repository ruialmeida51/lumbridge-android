package com.eyther.lumbridge.model.loan

import java.time.LocalDate

data class LoanUi(
    val id: Long = -1,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val initialLoanAmount: Float,
    val currentLoanAmount: Float,
    val loanCategoryUi: LoanCategoryUi,
    val loanInterestRateUi: LoanInterestRateUi
) {
    val paidLoanAmount: Float
        get() = initialLoanAmount - currentLoanAmount
}
