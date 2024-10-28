package com.eyther.lumbridge.domain.mapper.snapshotsalary

import com.eyther.lumbridge.data.model.snapshotsalary.local.SnapshotNetSalaryCached
import com.eyther.lumbridge.domain.model.snapshotsalary.SnapshotNetSalaryDomain

fun SnapshotNetSalaryDomain.toCached() = SnapshotNetSalaryCached(
    snapshotId = snapshotId,
    year = year,
    month = month,
    amount = netSalary
)
