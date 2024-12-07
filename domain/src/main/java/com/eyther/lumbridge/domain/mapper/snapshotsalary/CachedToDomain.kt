package com.eyther.lumbridge.domain.mapper.snapshotsalary

import com.eyther.lumbridge.data.model.snapshotsalary.local.SnapshotNetSalaryCached
import com.eyther.lumbridge.domain.model.netsalary.allocation.MoneyAllocation
import com.eyther.lumbridge.domain.model.netsalary.allocation.MoneyAllocationType
import com.eyther.lumbridge.domain.model.snapshotsalary.SnapshotNetSalaryDomain

fun SnapshotNetSalaryCached.toDomain() = SnapshotNetSalaryDomain(
    snapshotId = snapshotId,
    year = year,
    month = month,
    netSalary = amount,
    moneyAllocations = listOfNotNull(
        savingsPercentage?.let { MoneyAllocation(MoneyAllocationType.Savings, it, amount) },
        necessitiesPercentage?.let { MoneyAllocation(MoneyAllocationType.Necessities, it, amount) },
        luxuriesPercentage?.let { MoneyAllocation(MoneyAllocationType.Luxuries, it, amount) }
    ),
    foodCardAmount = foodCardAmount
)

fun List<SnapshotNetSalaryCached>.toDomain() = map { it.toDomain() }
