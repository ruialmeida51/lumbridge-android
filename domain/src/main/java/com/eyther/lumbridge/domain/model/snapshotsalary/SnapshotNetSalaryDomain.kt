package com.eyther.lumbridge.domain.model.snapshotsalary

import com.eyther.lumbridge.domain.model.netsalary.allocation.MoneyAllocation

data class SnapshotNetSalaryDomain(
    val snapshotId: Long = -1,
    val year: Int,
    val month: Int,
    val moneyAllocations: List<MoneyAllocation>,
    val netSalary: Float
)
