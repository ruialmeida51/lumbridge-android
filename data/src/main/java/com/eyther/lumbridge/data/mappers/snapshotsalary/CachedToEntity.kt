package com.eyther.lumbridge.data.mappers.snapshotsalary

import com.eyther.lumbridge.data.model.snapshotsalary.entity.SnapshotNetSalaryEntity
import com.eyther.lumbridge.data.model.snapshotsalary.local.SnapshotNetSalaryCached

fun SnapshotNetSalaryCached.toEntity() = SnapshotNetSalaryEntity(
    year = year,
    month = month,
    amount = amount,
    savingsPercentage = savingsPercentage,
    necessitiesPercentage = necessitiesPercentage,
    luxuriesPercentage = luxuriesPercentage,
    foodCardAmount = foodCardAmount
)
