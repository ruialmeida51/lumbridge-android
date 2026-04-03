package com.eyther.lumbridge.usecase.recurringpayments

import com.eyther.lumbridge.domain.model.expenses.ExpenseDomain
import com.eyther.lumbridge.domain.model.recurringpayments.RecurringPaymentDomain
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
    suspend operator fun invoke(): List<RecurringPaymentDomain> {
        val recurringPaymentsPaid = mutableListOf<RecurringPaymentDomain>()
        val recurringPayments = getRecurringPaymentsUseCase()

        recurringPayments.forEach { paymentDomain ->
            if (paymentDomain.shouldPay()) {
                payRecurringPayment(paymentDomain)
                updateRecurringPaymentLastPaymentDate(paymentDomain)

                if (paymentDomain.shouldNotifyWhenPaid) {
                    recurringPaymentsPaid.add(paymentDomain)
                }
            }
        }

        return recurringPaymentsPaid
    }

    /**
     * Create a new expense from a recurring payment and save it.
     */
    private suspend fun payRecurringPayment(paymentDomain: RecurringPaymentDomain) {
        val expense = ExpenseDomain(
            categoryType = paymentDomain.categoryTypes,
            expenseName = paymentDomain.label,
            expenseAmount = paymentDomain.amountToPay,
            allocation = paymentDomain.allocationType,
            date = LocalDate.now(),
        )

        saveExpenseUseCase(expense)
    }

    /**
     * Update the last payment date of a recurring payment to the current date.
     *
     * @param paymentDomain The recurring payment to update.
     */
    private suspend fun updateRecurringPaymentLastPaymentDate(paymentDomain: RecurringPaymentDomain) {
        saveRecurringPaymentUseCase(
            paymentDomain.copy(
                lastPaymentDate = LocalDate.now()
            )
        )
    }

    /**
     * Determines if a recurring payment should be paid. It should be paid if the next payment date is today or
     * in the past in relation to the current date.
     *
     * @return True if the recurring payment should be paid, false otherwise.
     */
    private fun RecurringPaymentDomain.shouldPay(): Boolean {
        return isWithinPaymentDate(this) && !hasAlreadyBeenPaid(this)
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
