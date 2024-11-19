package com.eyther.lumbridge.features.tools.recurringpayments.viewmodel.edit.delegate

import com.eyther.lumbridge.features.tools.recurringpayments.model.edit.EditRecurringPaymentScreenInputState
import kotlinx.coroutines.flow.StateFlow

interface IEditRecurringPaymentInputHandler {
    val inputState: StateFlow<EditRecurringPaymentScreenInputState>

    /**
     * Methods called on different input changes. Each one updates the input state with
     * the new value and performs any necessary validation.
     */
    fun onNotifyMeWhenPaidChanged(shouldNotifyWhenPaid: Boolean)
    fun onPaymentNamedChanged(paymentName: String?)
    fun onPaymentAmountChanged(paymentAmount: Float?)
    fun onPaymentTypeChanged(typeOrdinal: Int?)
    fun onPaymentAllocationChanged(allocationOrdinal: Int?)
    fun onPaymentStartDateChanged(paymentStartDate: Long?)
    fun onPaymentSurplusOrExpenseChanged(choiceOrdinal: Int)
    fun onPeriodicityTypeChanged(periodicityTypeOrdinal: Int?)
    fun onNumOfDaysChanged(numOfDays: Int?)
    fun onNumOfWeeksChanged(numOfWeeks: Int?)
    fun onNumOfMonthsChanged(numOfMonths: Int?)
    fun onNumOfYearsChanged(numOfYears: Int?)
    fun onDayOfWeekChanged(dayOfWeekOrdinal: Int?)
    fun onDayOfMonthChanged(dayOfMonth: Int?)
    fun onMonthOfYearChanged(monthOfYearOrdinal: Int?)

    /**
     * Validates the entire input state and returns an error message if the input state is invalid.
     * @return an error message if the percentages are invalid, null otherwise.
     * @see EditRecurringPaymentScreenInputState
     */
    fun validateInput(inputState: EditRecurringPaymentScreenInputState): Boolean

    /**
     * Checks if we have enough information available to enable the button.
     * @return true if we have enough information to enable the button, false otherwise.
     * @see EditRecurringPaymentScreenInputState
     */
    fun shouldEnableSaveButton(inputState: EditRecurringPaymentScreenInputState): Boolean

    /**
     * Helper function to update the input state.
     * @param update the lambda to update the input state.
     * @see EditRecurringPaymentScreenInputState
     */
    fun updateInput(update: (EditRecurringPaymentScreenInputState) -> EditRecurringPaymentScreenInputState)
}
