package com.eyther.lumbridge.data.model.expenses.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

const val EXPENSES_CATEGORY_TABLE_NAME = "expenses_category"

/**
 * Represents a cached version of a month detail.
 *
 * The foreign key definition indicates that this entity is a child of [ExpensesMonthEntity],
 * and that if the parent is deleted, this entity should also be deleted.
 *
 * The indices are used to speed up queries on the parentId column.
 *
 * @property categoryId The unique identifier for this category.
 * @property parentMonthId The unique identifier for the parent month.
 * @property categoryTypeOrdinal The category of expenses.
 */
@Entity(
    tableName = EXPENSES_CATEGORY_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = ExpensesMonthEntity::class,
            parentColumns = ["monthId"],
            childColumns = ["parentMonthId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("parentMonthId")]
)
data class ExpensesCategoryEntity(
    @PrimaryKey(autoGenerate = true) val categoryId:  Long = 0,
    val parentMonthId: Long,
    val categoryTypeOrdinal: Int
)
