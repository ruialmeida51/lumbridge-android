package com.eyther.lumbridge.model.expenses

import com.eyther.lumbridge.domain.time.toLocalDate
import com.eyther.lumbridge.domain.time.toMonthYearDateString
import com.eyther.lumbridge.extensions.kotlin.capitalise
import java.time.Month
import java.time.Year

data class ExpensesMonthUi(
    val id: Long = 0,
    val month: Month,
    val year: Year,
    val spent: Float = -1f,
    val remainder: Float = -1f,
    val expanded: Boolean = false,
    val categoryExpenses: List<ExpensesCategoryUi>
) {

    fun getDateWithLocale(): String {
        return (year to month).toLocalDate().toMonthYearDateString().capitalise()
    }
}
