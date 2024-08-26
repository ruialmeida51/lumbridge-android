package com.eyther.lumbridge.data.model.expenses.query

import androidx.room.Embedded
import androidx.room.Relation
import com.eyther.lumbridge.data.model.expenses.entity.ExpensesCategoryEntity
import com.eyther.lumbridge.data.model.expenses.entity.ExpensesMonthEntity

data class ExpensesForMonthWithCategoryAndDetails(
    @Embedded
    val expensesMonthEntity: ExpensesMonthEntity,

    @Relation(
        parentColumn = "monthId",
        entityColumn = "parentMonthId",
        entity = ExpensesCategoryEntity::class
    )
    val categories: List<ExpensesForCategoryWithDetails> = emptyList()
)
