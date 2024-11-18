package com.eyther.lumbridge.usecase.loan

import com.eyther.lumbridge.domain.model.loan.LoanDomain
import com.eyther.lumbridge.mapper.loan.toDomain
import com.eyther.lumbridge.model.expenses.ExpenseUi
import com.eyther.lumbridge.model.expenses.ExpensesCategoryTypesUi
import com.eyther.lumbridge.model.finance.MoneyAllocationTypeUi
import com.eyther.lumbridge.model.loan.LoanCalculationUi
import com.eyther.lumbridge.model.loan.LoanCategoryUi
import com.eyther.lumbridge.model.loan.LoanUi
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
    suspend operator fun invoke(): List<Pair<LoanUi, LoanCalculationUi>> {
        val loansPaid = mutableListOf<Pair<LoanUi, LoanCalculationUi>>()
        val locale = getLocaleOrDefault()
        val loans = getAllLoansUseCase(locale)

        loans.forEach { (loan, loanCalculationUi) ->
            if (loan.shouldPay()) {
                addExpense(loan, loanCalculationUi)
                updateLoan(loan, loanCalculationUi)

                if (loan.shouldNotifyWhenPaid) {
                    loansPaid.add(loan to loanCalculationUi)
                }
            }
        }

        return loansPaid
    }

    /**
     * Adds a loan to the expenses list, flagged as a Housing expense.
     *
     * @param loan The loan to be paid.
     * @param loanCalculationUi The loan calculation UI to be paid.
     */
    private suspend fun addExpense(loan: LoanUi, loanCalculationUi: LoanCalculationUi) {
        val expense = ExpenseUi(
            categoryType = if (loan.loanCategoryUi == LoanCategoryUi.House) {
                ExpensesCategoryTypesUi.Housing
            } else {
                ExpensesCategoryTypesUi.Other
            },
            expenseName = loan.name,
            expenseAmount = loanCalculationUi.monthlyPayment,
            date = LocalDate.now(),
            allocationTypeUi = MoneyAllocationTypeUi.Necessities()
        )

        saveExpenseUseCase(expense)
    }

    /**
     * Updates the last payment date of a loan to the current date and adds a payment to the loan.
     *
     * @param loan The loan to update.
     * @param loanCalculationUi The loan calculation UI to update.
     */
    private suspend fun updateLoan(loan: LoanUi, loanCalculationUi: LoanCalculationUi) {
        addPaymentToLoanUseCase(
            loanUi = loan.copy(lastAutoPayDate = LocalDate.now()),
            loanCalculationUi = loanCalculationUi,
        )
    }

    /**
     * Determines if a loan should be paid based on the last payment date and the current date.
     *
     * @return True if the loan should be paid, false otherwise.
     */
    private fun LoanUi.shouldPay(): Boolean {
        if (!shouldAutoAddToExpenses) return false

        val loanDomain = toDomain()

        return isWithinPaymentDate(loanDomain) && !hasAlreadyPaid(loanDomain)
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
