package com.eyther.lumbridge.domain.model.recurringpayments.model

import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryTypes
import com.eyther.lumbridge.shared.time.extensions.isAfterOrEqual
import com.eyther.lumbridge.shared.time.model.Periodicity
import java.time.LocalDate

/**
 * Represents a recurring payment that happens at a given periodicity.
 *
 * @param id The unique identifier of the recurring payment.
 * @param startDate The date the recurring payment should start.
 * @param lastPaymentDate The date of the last payment made, null if no payments have been made.
 * @param periodicity The periodicity between each payment.
 * @param label A user given label for the recurring payment.
 * @param shouldNotifyWhenPaid If true, the user should be notified when the payment is made.
 */
data class RecurringPaymentDomain(
    val id: Long = -1,
    val startDate: LocalDate,
    val lastPaymentDate: LocalDate?,
    val periodicity: Periodicity,
    val label: String,
    val amountToPay: Float,
    val categoryTypes: ExpensesCategoryTypes,
    val shouldNotifyWhenPaid: Boolean
)
