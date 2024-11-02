package com.eyther.lumbridge.model.expenses

import com.eyther.lumbridge.extensions.kotlin.capitalise
import com.eyther.lumbridge.shared.time.extensions.toLocalDate
import com.eyther.lumbridge.shared.time.extensions.toMonthYearDateString
import java.time.Month
import java.time.Year

data class ExpensesMonthUi(
    val categoryExpenses: List<ExpensesCategoryUi>,
    val snapshotMonthlyNetSalary: Float = 0f,
    val month: Month,
    val year: Year,
    val spent: Float = -1f,
    val remainder: Float = -1f,
    val expanded: Boolean = false
) {

    fun getDateWithLocale(): String {
        return (year to month).toLocalDate().toMonthYearDateString().capitalise()
    }
}