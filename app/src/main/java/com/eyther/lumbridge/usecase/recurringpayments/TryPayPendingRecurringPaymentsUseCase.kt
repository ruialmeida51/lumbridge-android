package com.eyther.lumbridge.usecase.recurringpayments

import com.eyther.lumbridge.domain.model.recurringpayments.RecurringPaymentDomain
import com.eyther.lumbridge.mapper.recurringpayments.toDomain
import com.eyther.lumbridge.model.expenses.ExpenseUi
import com.eyther.lumbridge.model.recurringpayments.RecurringPaymentUi
import com.eyther.lumbridge.shared.time.extensions.isAfterOrEqual
import com.eyther.lumbridge.shared.time.extensions.isBeforeOrEqual
import com.eyther.lumbridge.usecase.expenses.SaveExpenseUseCase
import java.time.LocalDate
import javax.inject.Inject

class TryPayPendingRecurringPaymentsUseCase @Inject constructor(
    private val getRecurringPaymentsUseCase: GetRecurringPaymentsUseCase,
    private val saveRecurringPaymentUseCase: SaveRecurringPaymentUseCase,
    private val saveExpenseUseCase: SaveExpenseUseCase
) {
    /**
     * Tries to pay all pending recurring payments.
     *
     * A recurring payment is considered pending if the next payment date is today or in the past in relation to the current date.
     * The payment is done by creating a new expense from the recurring payment and saving it.
     *
     * @return the list of payments that were paid to notify the user, empty if the flag to notify the user is false.
     */
    suspend operator fun invoke(): List<RecurringPaymentUi> {
        val recurringPaymentsPaid = mutableListOf<RecurringPaymentUi>()
        val recurringPayments = getRecurringPaymentsUseCase()

        recurringPayments.forEach { paymentUi ->
            if (paymentUi.shouldPay()) {
                payRecurringPayment(paymentUi)
                updateRecurringPaymentLastPaymentDate(paymentUi)

                if (paymentUi.shouldNotifyWhenPaid) {
                    recurringPaymentsPaid.add(paymentUi)
                }
            }
        }

        return recurringPaymentsPaid
    }

    /**
     * Create a new expense from a recurring payment and save it.
     */
    private suspend fun payRecurringPayment(recurringPaymentUi: RecurringPaymentUi) {
        val expense = ExpenseUi(
            categoryType = recurringPaymentUi.categoryTypesUi,
            expenseName = recurringPaymentUi.label,
            expenseAmount = recurringPaymentUi.amountToPay,
            allocationTypeUi = recurringPaymentUi.allocationTypeUi,
            date = LocalDate.now(),
        )

        saveExpenseUseCase(expense)
    }

    /**
     * Update the last payment date of a recurring payment to the current date.
     *
     * @param recurringPaymentUi The recurring payment to update.
     */
    private suspend fun updateRecurringPaymentLastPaymentDate(recurringPaymentUi: RecurringPaymentUi) {
        saveRecurringPaymentUseCase(
            recurringPaymentUi.copy(
                mostRecentPaymentDate = LocalDate.now()
            )
        )
    }

    /**
     * Determines if a recurring payment should be paid. It should be paid if the next payment date is today or
     * in the past in relation to the current date.
     *
     * @return True if the recurring payment should be paid, false otherwise.
     */
    private fun RecurringPaymentUi.shouldPay(): Boolean {
        val paymentDomain = toDomain()

        return isWithinPaymentDate(paymentDomain) && !hasAlreadyBeenPaid(paymentDomain)
    }

    /**
     * Calculates the next payment date for a recurring payment. If the last payment date is null, the start date is used.
     *
     * @return The next payment date for the recurring payment.
     */
    private fun isWithinPaymentDate(
        paymentDomain: RecurringPaymentDomain
    ): Boolean {
        return paymentDomain.periodicity
            .getNextDate(paymentDomain.tryGetMostRecentPaymentDate)
            .isBeforeOrEqual(LocalDate.now())
    }

    /**
     * Determines if a recurring payment has already been paid. A payment is considered paid if the last payment date is
     * today or in the future in relation to the current date.
     *
     * @return True if the recurring payment has already been paid, false otherwise.
     */
    private fun hasAlreadyBeenPaid(paymentDomain: RecurringPaymentDomain): Boolean {
        if (paymentDomain.lastPaymentDate == null) return false

        return paymentDomain.lastPaymentDate?.isAfterOrEqual(LocalDate.now()) == true
    }
}
