package com.eyther.lumbridge.data.model.expenses.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

const val EXPENSES_DETAILED_TABLE_NAME = "expenses_detailed"

/**
 * Represents a cached version of a detailed expense.
 *
 * The foreign key definition indicates that this entity is a child of [ExpensesCategoryEntity],
 * and that if the parent is deleted, this entity should also be deleted.
 *
 * The indices are used to speed up queries on the parentCategoryId column.
 *
 * @property detailId The unique identifier for this detailed expense.
 * @property parentCategoryId The unique identifier for the parent category.
 * @property expenseName The name of the expense.
 * @property expenseAmount The amount of the expense.
 */
@Entity(
    tableName = EXPENSES_DETAILED_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = ExpensesCategoryEntity::class,
            parentColumns = ["categoryId"],
            childColumns = ["parentCategoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("parentCategoryId")]
)
data class ExpensesDetailedEntity(
    @PrimaryKey(autoGenerate = true) val detailId: Long = 0,
    val parentCategoryId: Long,
    val expenseName: String,
    val expenseAmount: Float
)
