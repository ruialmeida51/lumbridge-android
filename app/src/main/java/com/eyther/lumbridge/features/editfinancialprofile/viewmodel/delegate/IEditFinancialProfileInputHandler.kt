package com.eyther.lumbridge.features.editfinancialprofile.viewmodel.delegate

import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileInputState
import com.eyther.lumbridge.ui.common.model.text.TextResource
import kotlinx.coroutines.flow.StateFlow

interface IEditFinancialProfileInputHandler {
    val inputState: StateFlow<EditFinancialProfileInputState>

    /**
     * Methods called on different input changes. Each one updates the input state with
     * the new value and performs any necessary validation.
     */
    fun onAnnualGrossSalaryChanged(annualGrossSalary: Float?)
    fun onFoodCardPerDiemChanged(foodCardPerDiem: Float?)
    fun onSavingsPercentageChanged(savingsPercentage: Int?)
    fun onNecessitiesPercentageChanged(necessitiesPercentage: Int?)
    fun onLuxuriesPercentageChanged(luxuriesPercentage: Int?)
    fun onNumberOfDependantsChanged(numberOfDependants: Int?)
    fun onSingleIncomeChanged(singleIncome: Boolean)
    fun onMarriedChanged(married: Boolean)
    fun onHandicappedChanged(handicapped: Boolean)

    /**
     * Validates the percentages and returns an error message if the percentages are invalid.
     * @return an error message if the percentages are invalid, null otherwise.
     * @see EditFinancialProfileInputState
     */
    fun validatePercentages(inputState: EditFinancialProfileInputState): TextResource?

    /**
     * Checks if we have enough information available to enable the button.
     * @return true if we have enough information to enable the button, false otherwise.
     * @see EditFinancialProfileInputState
     */
    fun shouldEnableSaveButton(inputState: EditFinancialProfileInputState): Boolean

    /**
     * Helper function to update the input state.
     * @param update the lambda to update the input state.
     * @see EditFinancialProfileInputState
     */
    fun updateInput(update: (EditFinancialProfileInputState) -> EditFinancialProfileInputState)
}
