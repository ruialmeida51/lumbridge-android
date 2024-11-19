package com.eyther.lumbridge.mapper.snapshotsalary

import com.eyther.lumbridge.domain.model.snapshotsalary.SnapshotNetSalaryDomain
import com.eyther.lumbridge.mapper.finance.toUi
import com.eyther.lumbridge.model.snapshotsalary.SnapshotNetSalaryUi

fun SnapshotNetSalaryDomain.toUi() = SnapshotNetSalaryUi(
    snapshotId = snapshotId,
    year = year,
    month = month,
    netSalary = netSalary,
    moneyAllocations = moneyAllocations.map { it.toUi() }
)
