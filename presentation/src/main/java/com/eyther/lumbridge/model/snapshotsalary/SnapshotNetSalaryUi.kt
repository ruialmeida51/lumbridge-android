package com.eyther.lumbridge.model.snapshotsalary

import com.eyther.lumbridge.model.finance.MoneyAllocationTypeUi

data class SnapshotNetSalaryUi(
    val snapshotId: Long = -1,
    val year: Int,
    val month: Int,
    val moneyAllocations: List<MoneyAllocationTypeUi>,
    val netSalary: Float,
    val foodCardAmount: Float
)
