package com.eyther.lumbridge.features.editloan.viewmodel.delegate

import com.eyther.lumbridge.R
import com.eyther.lumbridge.shared.time.extensions.toLocalDate
import com.eyther.lumbridge.extensions.kotlin.getErrorOrNull
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewState.Content
import com.eyther.lumbridge.features.editloan.model.EditLoanFixedTypeChoice
import com.eyther.lumbridge.features.editloan.model.EditLoanScreenInputState
import com.eyther.lumbridge.features.editloan.model.EditLoanVariableOrFixedChoice
import com.eyther.lumbridge.model.loan.LoanCategoryUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class EditLoanScreenInputHandler @Inject constructor() : IEditLoanScreenInputHandler {
    override val inputState = MutableStateFlow(EditLoanScreenInputState())

    override fun onNameChanged(name: String?) {
        updateInput { state ->
            state.copy(
                name = state.name.copy(
                    text = name,
                    error = name.getErrorOrNull(
                        R.string.edit_loan_profile_invalid_loan_name
                    )
                )
            )
        }
    }

    override fun onMortgageAmountChanged(mortgageAmount: Float?) {
        updateInput { state ->
            val loanAmount = mortgageAmount?.toString()

            state.copy(
                loanAmount = state.loanAmount.copy(
                    text = loanAmount,
                    error = loanAmount.getErrorOrNull(
                        R.string.edit_loan_profile_invalid_loan_amount
                    )
                )
            )
        }
    }

    override fun onEuriborRateChanged(euriborRate: Float?) {
        updateInput { state ->
            val euriborRateText = euriborRate?.toString()

            state.copy(
                euribor = state.euribor.copy(
                    text = euriborRateText,
                    error = euriborRateText.getErrorOrNull(
                        R.string.edit_loan_profile_invalid_euribor
                    )
                )
            )
        }
    }

    override fun onSpreadChanged(spread: Float?) {
        updateInput { state ->
            val spreadText = spread?.toString()

            state.copy(
                spread = state.spread.copy(
                    text = spreadText,
                    error = spreadText.getErrorOrNull(
                        R.string.edit_loan_profile_invalid_spread
                    )
                )
            )
        }
    }

    override fun onTanInterestRateChanged(tanInterestRate: Float?) {
        updateInput { state ->
            val tanInterestRateString = tanInterestRate?.toString()

            state.copy(
                tanInterestRate = state.tanInterestRate.copy(
                    text = tanInterestRateString,
                    error = tanInterestRateString.getErrorOrNull(
                        R.string.edit_loan_profile_invalid_interest_rate
                    )
                )
            )
        }
    }

    override fun onTaegInterestRateChanged(taegInterestRate: Float?) {
        updateInput { state ->
            val taegInterestRateString = taegInterestRate?.toString()

            state.copy(
                taegInterestRate = state.taegInterestRate.copy(
                    text = taegInterestRateString,
                    error = taegInterestRateString.getErrorOrNull(
                        R.string.edit_loan_profile_invalid_interest_rate
                    )
                )
            )
        }
    }

    override fun onFixedOrVariableLoanChanged(option: Int) {
        updateInput { state ->
            state.copy(
                fixedOrVariableLoanChoiceState = state.fixedOrVariableLoanChoiceState.copy(
                    selectedTab = option
                )
            )
        }
    }

    override fun onTanOrTaegLoanChanged(option: Int) {
        updateInput { state ->
            state.copy(
                tanOrTaegLoanChoiceState = state.tanOrTaegLoanChoiceState.copy(
                    selectedTab = option
                )
            )
        }
    }

    override fun onStartDateChanged(startDate: Long?) {
        updateInput { state ->
            state.copy(
                startDate = state.startDate.copy(
                    date = startDate?.toLocalDate(),
                    error = startDate?.toString().getErrorOrNull(
                        R.string.edit_loan_profile_invalid_start_date
                    )
                )
            )
        }
    }

    override fun onEndDateChanged(endDate: Long?) {
        updateInput { state ->
            state.copy(
                endDate = state.endDate.copy(
                    date = endDate?.toLocalDate(),
                    error = endDate?.toString().getErrorOrNull(
                        R.string.edit_loan_profile_invalid_end_date
                    )
                )
            )
        }
    }

    override fun onLoanCategoryChanged(ordinal: Int?) {
        updateInput { state ->
            state.copy(
                categoryUi = LoanCategoryUi.fromOrdinal(ordinal ?: 0)
            )
        }
    }

    override fun onAutomaticallyAddToExpensesChanged(automaticallyAddToExpenses: Boolean) {
        updateInput { state ->
            state.copy(
                shouldAutoAddToExpenses = automaticallyAddToExpenses
            )
        }
    }

    override fun onNotifyWhenPaidChanged(notifyWhenPaid: Boolean) {
        updateInput { state ->
            state.copy(
                shouldNotifyWhenPaid = notifyWhenPaid
            )
        }
    }

    override fun onPaymentDayChanged(paymentDay: Int?) {
        updateInput { state ->
            val paymentDayString = if (validatePaymentDay(paymentDay ?: 0)) {
                paymentDay?.toString()
            } else {
                null
            }

            state.copy(
                paymentDay = state.paymentDay.copy(
                    text = paymentDayString,
                    error = paymentDayString.getErrorOrNull(
                        R.string.edit_loan_profile_invalid_payment_day
                    )
                )
            )
        }
    }

    override fun validateInput(inputState: EditLoanScreenInputState): Boolean {
        val isMortgageTypeValid = when (inputState.fixedOrVariableLoanChoiceState.selectedTab) {
            EditLoanVariableOrFixedChoice.Variable.ordinal -> validateVariableLoan(inputState)
            EditLoanVariableOrFixedChoice.Fixed.ordinal -> validateFixedLoan(inputState)
            else -> false
        }

        val isPaymentDayValid = if (inputState.shouldAutoAddToExpenses) {
            validatePaymentDay(inputState.paymentDay.text?.toInt())
        } else {
            true
        }

        return inputState.loanAmount.isValid() &&
            inputState.loanAmount.isValid() &&
            inputState.startDate.isValid() &&
            inputState.endDate.isValid() &&
            inputState.name.isValid() &&
            isMortgageTypeValid &&
            isPaymentDayValid
    }

    private fun validateVariableLoan(inputState: EditLoanScreenInputState): Boolean {
        return inputState.euribor.isValid() && inputState.spread.isValid()
    }

    private fun validateFixedLoan(inputState: EditLoanScreenInputState): Boolean {
        return when (inputState.tanOrTaegLoanChoiceState.selectedTab) {
            EditLoanFixedTypeChoice.Tan.ordinal -> inputState.tanInterestRate.isValid()
            EditLoanFixedTypeChoice.Taeg.ordinal -> inputState.taegInterestRate.isValid()
            else -> false
        }
    }

    private fun validatePaymentDay(paymentDay: Int?): Boolean {
        return paymentDay != null && paymentDay in 1..31
    }

    override fun shouldEnableSaveButton(inputState: EditLoanScreenInputState): Boolean {
        return validateInput(inputState)
    }

    /**
     * Helper function to update the inputState state of the screen.
     *
     * @param update the function to update the content state.
     * @see Content
     */
    override fun updateInput(
        update: (EditLoanScreenInputState) -> EditLoanScreenInputState
    ) {
        inputState.update { currentState ->
            update(currentState)
        }
    }
}
