package com.eyther.lumbridge.domain.model.snapshotsalary

data class SnapshotNetSalaryDomain(
    val snapshotId: Long = -1,
    val year: Int,
    val month: Int,
    val netSalary: Float
)
