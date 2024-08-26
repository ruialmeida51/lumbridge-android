package com.eyther.lumbridge.data.model.expenses.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

const val EXPENSES_MONTH_TABLE_NAME = "expenses_month"

@Entity(
    tableName = EXPENSES_MONTH_TABLE_NAME,
    indices = [
        Index(value = ["month", "year"], unique = true)
    ]
)
data class ExpensesMonthEntity(
    @PrimaryKey(autoGenerate = true) val monthId: Long = 0,
    val month: Int,
    val year: Int
)
