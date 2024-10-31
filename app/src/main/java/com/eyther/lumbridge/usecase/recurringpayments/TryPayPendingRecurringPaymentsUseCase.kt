package com.eyther.lumbridge.usecase.recurringpayments

import com.eyther.lumbridge.mapper.recurringpayments.toDomain
import com.eyther.lumbridge.model.expenses.ExpenseUi
import com.eyther.lumbridge.model.recurringpayments.RecurringPaymentUi
import com.eyther.lumbridge.shared.di.model.Schedulers
import com.eyther.lumbridge.shared.time.extensions.isAfterOrEqual
import com.eyther.lumbridge.shared.time.model.Periodicity
import com.eyther.lumbridge.usecase.expenses.SaveExpenseUseCase
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class TryPayPendingRecurringPaymentsUseCase @Inject constructor(
    private val getRecurringPaymentsUseCase: GetRecurringPaymentsUseCase,
    private val saveExpenseUseCase: SaveExpenseUseCase,
    private val schedulers: Schedulers
) {
    suspend operator fun invoke() {
        val recurringPayments = getRecurringPaymentsUseCase()

        withContext(schedulers.cpu) {
            recurringPayments.forEach { payment ->
                if (shouldPay(payment.periodicity, payment.startDate, payment.lastPaymentDate)) {
                    payRecurringPayment(payment)
                }
            }
        }
    }

    /**
     * Create a new expense from a recurring payment and save it.
     */
    private suspend fun payRecurringPayment(recurringPaymentUi: RecurringPaymentUi) {
        val expense = ExpenseUi(
            categoryType = recurringPaymentUi.categoryTypesUi,
            expenseName = recurringPaymentUi.label,
            expenseAmount = recurringPaymentUi.amountToPay,
            date = LocalDate.now()
        )

        withContext(schedulers.io) {
            saveExpenseUseCase(expense)
        }
    }

    /**
     * Determines if a recurring payment should be paid. It should be paid if the next payment date is today or
     * in the past in relation to the current date.
     *
     * @return True if the recurring payment should be paid, false otherwise.
     */
    private fun shouldPay(
        periodicity: Periodicity,
        startDate: LocalDate,
        lastPaymentDate: LocalDate?
    ): Boolean =
        getNextPaymentDate(periodicity, startDate, lastPaymentDate)
            .isAfterOrEqual(LocalDate.now())

    /**
     * Calculates the next payment date for a recurring payment. If the last payment date is null, the start date is used.
     *
     * @return The next payment date for the recurring payment.
     */
    private fun getNextPaymentDate(
        periodicity: Periodicity,
        startDate: LocalDate,
        lastPaymentDate: LocalDate?
    ): LocalDate {
        val startFrom = lastPaymentDate ?: startDate
        return periodicity.getNextDate(startFrom)
    }
}