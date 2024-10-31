package com.eyther.lumbridge.model.recurringpayments

import com.eyther.lumbridge.model.expenses.ExpensesCategoryTypesUi
import com.eyther.lumbridge.shared.time.model.Periodicity
import java.time.LocalDate

data class RecurringPaymentUi(
    val id: Long = -1,
    val startDate: LocalDate,
    val lastPaymentDate: LocalDate?,
    val periodicity: Periodicity,
    val amountToPay: Float,
    val categoryTypesUi: ExpensesCategoryTypesUi,
    val label: String,
    val shouldNotifyWhenPaid: Boolean
)
