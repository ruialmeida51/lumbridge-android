package com.eyther.lumbridge.features.editmortgageprofile.viewmodel.delegate

import com.eyther.lumbridge.features.editmortgageprofile.model.EditMortgageProfileInputState
import com.eyther.lumbridge.model.mortgage.MortgageTypeUi
import kotlinx.coroutines.flow.StateFlow

interface IEditMortgageProfileInputHandler {
    val inputState: StateFlow<EditMortgageProfileInputState>

    /**
     * Methods called on different input changes. Each one updates the input state with
     * the new value and performs any necessary validation.
     */
    fun onMortgageAmountChanged(mortgageAmount: Float?)
    fun onEuriborRateChanged(euriborRate: Float?)
    fun onSpreadChanged(spread: Float?)
    fun onFixedInterestRateChanged(fixedInterestRate: Float?)
    fun onMortgageTypeChanged(mortgageType: MortgageTypeUi?)
    fun onStartDateChanged(startDate: Long?)
    fun onEndDateChanged(endDate: Long?)

    /**
     * Validates the entire input state and returns an error message if the input state is invalid.
     * @return an error message if the percentages are invalid, null otherwise.
     * @see EditMortgageProfileInputState
     */
    fun validateInput(inputState: EditMortgageProfileInputState): Boolean

    /**
     * Checks if we have enough information available to enable the button.
     * @return true if we have enough information to enable the button, false otherwise.
     * @see EditMortgageProfileInputState
     */
    fun shouldEnableSaveButton(inputState: EditMortgageProfileInputState): Boolean

    /**
     * Helper function to update the input state.
     * @param update the lambda to update the input state.
     * @see EditMortgageProfileInputState
     */
    fun updateInput(update: (EditMortgageProfileInputState) -> EditMortgageProfileInputState)
}