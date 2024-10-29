package com.eyther.lumbridge.domain.mapper.snapshotsalary

import com.eyther.lumbridge.data.model.snapshotsalary.local.SnapshotNetSalaryCached
import com.eyther.lumbridge.domain.model.snapshotsalary.SnapshotNetSalaryDomain

fun SnapshotNetSalaryCached.toDomain() = SnapshotNetSalaryDomain(
    snapshotId = snapshotId,
    year = year,
    month = month,
    netSalary = amount
)

fun List<SnapshotNetSalaryCached>.toDomain() = map { it.toDomain() }
