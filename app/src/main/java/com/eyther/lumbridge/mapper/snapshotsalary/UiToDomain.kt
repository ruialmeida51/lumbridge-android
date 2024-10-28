package com.eyther.lumbridge.mapper.snapshotsalary

import com.eyther.lumbridge.domain.model.snapshotsalary.SnapshotNetSalaryDomain
import com.eyther.lumbridge.model.snapshotsalary.SnapshotNetSalaryUi

fun SnapshotNetSalaryUi.toDomain() = SnapshotNetSalaryDomain(
    snapshotId = snapshotId,
    year = year,
    month = month,
    netSalary = netSalary
)
