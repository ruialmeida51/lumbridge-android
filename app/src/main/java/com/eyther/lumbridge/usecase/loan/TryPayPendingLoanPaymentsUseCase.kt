package com.eyther.lumbridge.usecase.loan

import com.eyther.lumbridge.mapper.loan.toDomain
import com.eyther.lumbridge.model.expenses.ExpenseUi
import com.eyther.lumbridge.model.expenses.ExpensesCategoryTypesUi
import com.eyther.lumbridge.model.loan.LoanCalculationUi
import com.eyther.lumbridge.model.loan.LoanCategoryUi
import com.eyther.lumbridge.model.loan.LoanUi
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
            categoryType = if (loan.loanCategoryUi == LoanCategoryUi.House) ExpensesCategoryTypesUi.Housing else  ExpensesCategoryTypesUi.Other,
            expenseName = loan.name,
            expenseAmount = loanCalculationUi.monthlyPayment,
            date = LocalDate.now()
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

        val mostRecentStartDate = loanDomain.lastAutoPayDate ?: LocalDate.now() // Last payment date or today
        val periodicity = Periodicity.EveryXMonths(numOfMonth = 1, dayOfMonth = loanDomain.paymentDay ?: 1) // Once a month, on a given day

        return getNextPaymentDate(
            mostRecentStartDate = mostRecentStartDate,
            periodicity = periodicity
        ).isBeforeOrEqual(LocalDate.now())
    }

    /**
     * Calculates the next payment date for a loan. The next payment date is calculated based on the most recent
     * payment date or today's date if the loan has not been paid yet but is flagged to be auto paid.
     *
     * @param mostRecentStartDate The most recent payment date for the loan.
     * @param periodicity The periodicity of the loan, at this moment only monthly payments are supported.
     */
    private fun getNextPaymentDate(
        mostRecentStartDate: LocalDate,
        periodicity: Periodicity
    ): LocalDate {
        return periodicity.getNextDate(mostRecentStartDate)
    }
}
