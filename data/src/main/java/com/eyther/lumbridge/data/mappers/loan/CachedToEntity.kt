package com.eyther.lumbridge.data.mappers.loan

import com.eyther.lumbridge.data.model.loan.entity.LoanEntity
import com.eyther.lumbridge.data.model.loan.local.LoanCached

fun LoanCached.toEntity() = LoanEntity(
    name = name,
    startDate = startDate,
    endDate = endDate,
    currentAmount = currentAmount,
    initialAmount = initialAmount,
    fixedTaegInterestRate = fixedTaegInterestRate,
    variableEuribor = variableEuribor,
    variableSpread = variableSpread,
    fixedTanInterestRate = fixedTanInterestRate,
    loanTypeOrdinal = loanTypeOrdinal,
    loanCategoryOrdinal = loanCategoryOrdinal
)
