package com.eyther.lumbridge.data.model.expenses.query

import androidx.room.Embedded
import androidx.room.Relation
import com.eyther.lumbridge.data.model.expenses.entity.ExpensesCategoryEntity
import com.eyther.lumbridge.data.model.expenses.entity.ExpensesDetailedEntity

data class ExpensesForCategoryWithDetails(
    @Embedded
    val expensesCategoryEntity: ExpensesCategoryEntity,

    @Relation(
        parentColumn = "categoryId",
        entityColumn = "parentCategoryId",
        entity = ExpensesDetailedEntity::class
    )
    val detailedExpenses: List<ExpensesDetailedEntity> = emptyList()
)
