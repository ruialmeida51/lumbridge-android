package com.eyther.lumbridge.model.recurringpayments

import com.eyther.lumbridge.model.finance.MoneyAllocationTypeUi
import com.eyther.lumbridge.model.expenses.ExpensesCategoryTypesUi
import com.eyther.lumbridge.model.time.PeriodicityUi
import java.time.LocalDate

data class RecurringPaymentUi(
    val id: Long = -1,
    val label: String,
    val amountToPay: Float,
    val startDate: LocalDate,
    val mostRecentPaymentDate: LocalDate?,
    val periodicity: PeriodicityUi,
    val categoryTypesUi: ExpensesCategoryTypesUi,
    val allocationTypeUi: MoneyAllocationTypeUi,
    val shouldNotifyWhenPaid: Boolean
)
