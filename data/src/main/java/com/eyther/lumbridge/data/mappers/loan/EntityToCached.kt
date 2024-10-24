package com.eyther.lumbridge.data.mappers.loan

import com.eyther.lumbridge.data.model.loan.entity.LoanEntity
import com.eyther.lumbridge.data.model.loan.local.LoanCached

fun LoanEntity.toCached() = LoanCached(
    id = loanId,
    name = name,
    startDate = startDate,
    endDate = endDate,
    amount = amount,
    fixedTaegInterestRate = fixedTaegInterestRate,
    variableEuribor = variableEuribor,
    variableSpread = variableSpread,
    fixedTanInterestRate = fixedTanInterestRate,
    loanTypeOrdinal = loanTypeOrdinal,
    loanCategoryOrdinal = loanCategoryOrdinal
)
