package com.eyther.lumbridge.usecase.loan

import com.eyther.lumbridge.domain.model.expenses.ExpenseDomain
import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryTypes
import com.eyther.lumbridge.domain.model.loan.LoanCalculation
import com.eyther.lumbridge.domain.model.loan.LoanCategory
import com.eyther.lumbridge.domain.model.loan.LoanDomain
import com.eyther.lumbridge.domain.model.netsalary.allocation.MoneyAllocationType
import com.eyther.lumbridge.shared.time.extensions.isAfterOrEqual
import com.eyther.lumbridge.shared.time.extensions.isBeforeOrEqual
import com.eyther.lumbridge.shared.time.model.Periodicity
import com.eyther.lumbridge.usecase.expenses.SaveExpenseUseCase
import com.eyther.lumbridge.usecase.user.profile.GetLocaleOrDefault
import java.time.LocalDate
import javax.inject.Inject

class TryPayPendingLoanPaymentsUseCase @Inject constructor(
    private val getAllLoansUseCase: GetAllLoansUseCase,
    private val saveExpenseUseCase: SaveExpenseUseCase,
    private val addPaymentToLoanUseCase: AddPaymentToLoanUseCase,
    private val getLocaleOrDefault: GetLocaleOrDefault
) {

    /**
     * Tries to pay all pending loans.
     *
     * A loan is considered pending if the next payment date is today or in the past in relation to the current date.
     * The payment is done by creating a new expense from the loan and saving it.
     *
     * @return the list of loans that were paid to notify the user, empty if the flag to notify the user is false.
     */
    suspend operator fun invoke(): List<Pair<LoanDomain, LoanCalculation>> {
        val loansPaid = mutableListOf<Pair<LoanDomain, LoanCalculation>>()
        val locale = getLocaleOrDefault()
        val loans = getAllLoansUseCase(locale)

        loans.forEach { (loan, loanCalculation) ->
            if (loan.shouldPay()) {
                addExpense(loan, loanCalculation)
                updateLoan(loan, loanCalculation)

                if (loan.shouldNotifyWhenPaid) {
                    loansPaid.add(loan to loanCalculation)
                }
            }
        }

        return loansPaid
    }

    /**
     * Adds a loan to the expenses list, flagged as a Housing expense.
     *
     * @param loan The loan to be paid.
     * @param loanCalculation The loan calculation to be paid.
     */
    private suspend fun addExpense(loan: LoanDomain, loanCalculation: LoanCalculation) {
        val expense = ExpenseDomain(
            id = -1,
            categoryType = if (loan.loanCategory == LoanCategory.HOUSE) {
                ExpensesCategoryTypes.Housing
            } else {
                ExpensesCategoryTypes.Other
            },
            expenseName = loan.name,
            expenseAmount = loanCalculation.monthlyPayment,
            date = LocalDate.now(),
            allocation = MoneyAllocationType.Necessities
        )

        saveExpenseUseCase(expense)
    }

    /**
     * Updates the last payment date of a loan to the current date and adds a payment to the loan.
     *
     * @param loan The loan to update.
     * @param loanCalculation The loan calculation to update.
     */
    private suspend fun updateLoan(loan: LoanDomain, loanCalculation: LoanCalculation) {
        addPaymentToLoanUseCase(
            loanDomain = loan.copy(lastAutoPayDate = LocalDate.now()),
            loanCalculation = loanCalculation,
        )
    }

    /**
     * Determines if a loan should be paid based on the last payment date and the current date.
     *
     * @return True if the loan should be paid, false otherwise.
     */
    private fun LoanDomain.shouldPay(): Boolean {
        if (!shouldAutoAddToExpenses) return false

        return isWithinPaymentDate(this) && !hasAlreadyPaid(this)
    }

    /**
     * Checks if a Loan is within the payment date range. A loan is within the payment date range if the next payment
     * date is today or in the past in relation to the current date.
     *
     * @param loanDomain The loan to check.
     * @param periodicity The periodicity of the loan, at this moment only monthly payments are supported.
     */
    private fun isWithinPaymentDate(
        loanDomain: LoanDomain,
        periodicity: Periodicity = Periodicity.EveryXMonths(numOfMonth = 1, dayOfMonth = loanDomain.paymentDay ?: 1)
    ): Boolean {
        val now = LocalDate.now()

        return periodicity
            .getNextDate(loanDomain.lastAutoPayDate ?: now)
            .isBeforeOrEqual(now)
    }

    /**
     * Determines if a loan has already been paid. A loan is considered paid if the last payment date is today or in the
     * future in relation to the current date. If there's no last payment date it means the loan has not been paid yet.
     *
     * @param loanDomain The loan to check.
     */
    private fun hasAlreadyPaid(
        loanDomain: LoanDomain
    ): Boolean {
        if (loanDomain.lastAutoPayDate == null) return false

        return loanDomain.lastAutoPayDate?.isAfterOrEqual(LocalDate.now()) == true
    }
}
