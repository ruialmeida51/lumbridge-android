package com.eyther.lumbridge.domain.model.loan

import com.eyther.lumbridge.shared.time.extensions.monthsUntil
import java.time.LocalDate

/**
 * Represents a loan in the domain layer. The parameters are the following:
 *
 * @param id The id of the loan. Defaults to -1 when creating a new one.
 * @param name The name of the loan, e.g. "Car Loan".
 * @param startDate The date when the loan was taken, when the contract was made.
 * @param currentPaymentDate The date when the current payment is due. This date is hidden from the user and it's just an internal information
 * to calculate the remaining months between the current date and the end date.
 * @param endDate The date when the loan is expected to be paid off.
 * @param initialAmount The initial amount of the loan.
 * @param currentAmount The current amount of the loan. This amount is updated every time a payment is made.
 * @param loanInterestRate The interest rate of the loan, represented by a percentage.
 * @param loanType The type of the loan, can be fixed or variable.
 * @param loanCategory The category of the loan, e.g. "House", "Car", "Personal", "Other".
 * @param shouldNotifyWhenPaid True if the user wants to be notified when the loan is paid off, false otherwise.
 * @param shouldAutoAddToExpenses True if the user wants the loan to be automatically added to the expenses, false otherwise.
 * @param lastAutoPayDate The last date when the loan was automatically paid. This is used to calculate the next payment date.
 * @param paymentDay The day of the month when the payment is due, if the user enabled automatic payments.
 */
data class LoanDomain(
    val id: Long = -1,
    val name: String,
    val startDate: LocalDate,
    val currentPaymentDate: LocalDate,
    val endDate: LocalDate,
    val initialAmount: Float,
    val currentAmount: Float,
    val loanInterestRate: LoanInterestRate,
    val loanType: LoanType,
    val loanCategory: LoanCategory,
    val shouldNotifyWhenPaid: Boolean,
    val shouldAutoAddToExpenses: Boolean,
    val lastAutoPayDate: LocalDate? = null,
    val paymentDay: Int? = null
) {
    val remainingMonths: Int
        get() = currentPaymentDate.monthsUntil(endDate)
}
