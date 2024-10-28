package com.eyther.lumbridge.data.model.snapshotsalary.local

data class SnapshotNetSalaryCached(
    val snapshotId: Long = -1,
    val year: Int,
    val month: Int,
    val amount: Float
)
