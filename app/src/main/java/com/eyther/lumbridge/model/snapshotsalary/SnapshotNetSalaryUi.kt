package com.eyther.lumbridge.model.snapshotsalary

data class SnapshotNetSalaryUi(
    val snapshotId: Long = -1,
    val year: Int,
    val month: Int,
    val netSalary: Float
)
