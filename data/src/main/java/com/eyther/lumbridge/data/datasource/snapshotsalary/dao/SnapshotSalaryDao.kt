package com.eyther.lumbridge.data.datasource.snapshotsalary.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.eyther.lumbridge.data.model.snapshotsalary.entity.SNAPSHOT_NET_SALARY_TABLE_NAME
import com.eyther.lumbridge.data.model.snapshotsalary.entity.SnapshotNetSalaryEntity

@Dao
interface SnapshotSalaryDao {
    @Transaction
    @Insert
    suspend fun insertSnapshotNetSalary(snapshotNetSalaryEntity: SnapshotNetSalaryEntity): Long

    @Transaction
    @Update
    suspend fun updateSnapshotNetSalary(snapshotNetSalaryEntity: SnapshotNetSalaryEntity): Int

    @Transaction
    @Query("SELECT * FROM $SNAPSHOT_NET_SALARY_TABLE_NAME")
    suspend fun getAllSnapshotNetSalaries(): List<SnapshotNetSalaryEntity>

    @Transaction
    @Query("SELECT * FROM $SNAPSHOT_NET_SALARY_TABLE_NAME WHERE year = :year AND month = :month")
    suspend fun getSnapshotNetSalaryByYearMonth(year: Int, month: Int): SnapshotNetSalaryEntity?
}
