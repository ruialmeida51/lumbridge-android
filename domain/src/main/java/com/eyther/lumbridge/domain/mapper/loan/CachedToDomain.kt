package com.eyther.lumbridge.domain.mapper.loan

import com.eyther.lumbridge.data.model.loan.local.LoanCached
import com.eyther.lumbridge.domain.model.loan.Loan
import com.eyther.lumbridge.domain.model.loan.LoanCategory
import com.eyther.lumbridge.domain.model.loan.LoanInterestRate
import com.eyther.lumbridge.domain.model.loan.LoanType
import com.eyther.lumbridge.shared.time.extensions.toLocalDate

fun LoanCached.toDomain(): Loan {
    val loanType = LoanType.entries[loanTypeOrdinal]
    val loanCategory = LoanCategory.entries[loanCategoryOrdinal]

    return Loan(
        id = id,
        name = name,
        startDate = startDate.toLocalDate(),
        endDate = endDate.toLocalDate(),
        currentAmount = currentAmount,
        initialAmount = initialAmount,
        loanInterestRate = LoanInterestRate.fromLoanType(
            loanType = LoanType.entries[loanTypeOrdinal],
            variableEuribor = variableEuribor,
            variableSpread = variableSpread,
            fixedTanInterestRate = fixedTanInterestRate,
            fixedTaegInterestRate = fixedTaegInterestRate
        ),
        loanType = loanType,
        loanCategory = loanCategory,
        shouldNotifyWhenPaid = shouldNotifyWhenPaid,
        shouldAutoAddToExpenses = shouldAutoAddToExpenses,
        lastAutoPayDate = lastAutoPayDate?.toLocalDate(),
        paymentDay = paymentDay
    )
}

fun List<LoanCached>.toDomain() = map { it.toDomain() }
