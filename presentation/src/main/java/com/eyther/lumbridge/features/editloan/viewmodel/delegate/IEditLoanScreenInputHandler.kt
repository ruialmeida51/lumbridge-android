package com.eyther.lumbridge.features.editloan.viewmodel.delegate

import com.eyther.lumbridge.features.editloan.model.EditLoanScreenInputState
import kotlinx.coroutines.flow.StateFlow

interface IEditLoanScreenInputHandler {
    val inputState: StateFlow<EditLoanScreenInputState>

    /**
     * Methods called on different input changes. Each one updates the input state with
     * the new value and performs any necessary validation.
     */
    fun onNameChanged(name: String?)
    fun onLoanInitialAmountChanged(initialAmount: Float?)
    fun onLoanCurrentAmountChanged(currentAmount: Float?)
    fun onEuriborRateChanged(euriborRate: Float?)
    fun onSpreadChanged(spread: Float?)
    fun onTanInterestRateChanged(tanInterestRate: Float?)
    fun onTaegInterestRateChanged(taegInterestRate: Float?)
    fun onFixedOrVariableLoanChanged(option: Int)
    fun onTanOrTaegLoanChanged(option: Int)
    fun onStartDateChanged(startDate: Long?)
    fun onEndDateChanged(endDate: Long?)
    fun onLoanCategoryChanged(ordinal: Int?)
    fun onNotifyWhenPaidChanged(notifyWhenPaid: Boolean)
    fun onAutomaticallyAddToExpensesChanged(automaticallyAddToExpenses: Boolean)
    fun onPaymentDayChanged(paymentDay: Int?)

    /**
     * Validates the entire input state and returns an error message if the input state is invalid.
     * @return an error message if the percentages are invalid, null otherwise.
     * @see EditLoanScreenInputState
     */
    fun validateInput(inputState: EditLoanScreenInputState): Boolean

    /**
     * Checks if we have enough information available to enable the button.
     * @return true if we have enough information to enable the button, false otherwise.
     * @see EditLoanScreenInputState
     */
    fun shouldEnableSaveButton(inputState: EditLoanScreenInputState): Boolean

    /**
     * Helper function to update the input state.
     * @param update the lambda to update the input state.
     * @see EditLoanScreenInputState
     */
    fun updateInput(update: (EditLoanScreenInputState) -> EditLoanScreenInputState)
}
