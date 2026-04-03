package com.eyther.lumbridge.domain.model.expenses

import com.eyther.lumbridge.domain.model.netsalary.allocation.MoneyAllocationType

data class MonthAllocationDomain(
    val type: MoneyAllocationType,
    val allocated: Float,
    val spent: Float,
    val gained: Float
)
