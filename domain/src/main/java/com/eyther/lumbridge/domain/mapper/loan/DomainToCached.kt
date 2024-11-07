package com.eyther.lumbridge.domain.mapper.loan

import com.eyther.lumbridge.data.model.loan.local.LoanCached
import com.eyther.lumbridge.domain.model.loan.LoanDomain
import com.eyther.lumbridge.shared.time.extensions.toIsoLocalDateString

fun LoanDomain.toCached() = LoanCached(
    id = id,
    name = name,
    startDate = startDate.toIsoLocalDateString(),
    currentPaymentDate = currentPaymentDate.toIsoLocalDateString(),
    endDate = endDate.toIsoLocalDateString(),
    currentAmount = currentAmount,
    initialAmount = initialAmount,
    fixedTaegInterestRate = loanInterestRate.tryGetTaegInterestRate(),
    variableEuribor = loanInterestRate.tryGetEuribor(),
    variableSpread = loanInterestRate.tryGetSpread(),
    fixedTanInterestRate = loanInterestRate.tryGetTanInterestRate(),
    loanTypeOrdinal = loanType.ordinal,
    loanCategoryOrdinal = loanCategory.ordinal,
    shouldNotifyWhenPaid = shouldNotifyWhenPaid,
    shouldAutoAddToExpenses = shouldAutoAddToExpenses,
    lastAutoPayDate = lastAutoPayDate?.toIsoLocalDateString(),
    paymentDay = paymentDay
)
