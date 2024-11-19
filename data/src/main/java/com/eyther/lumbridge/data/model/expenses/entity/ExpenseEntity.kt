package com.eyther.lumbridge.data.model.expenses.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

const val EXPENSES_TABLE_NAME = "expenses"

@Entity(
    tableName = EXPENSES_TABLE_NAME,
    indices = [Index("expenseId")]
)
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val expenseId:  Long = 0,
    val categoryTypeOrdinal: Int,
    @ColumnInfo(defaultValue = "0") val allocationTypeOrdinal: Int = 0,
    val date: LocalDate,
    val amount: Float,
    val name: String
)
