package com.eyther.lumbridge.features.editfinancialprofile.viewmodel.delegate

import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileInputState
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewState.Content
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class EditFinancialProfileInputHandler @Inject constructor() : IEditFinancialProfileInputHandler {

    override val inputState = MutableStateFlow(EditFinancialProfileInputState())

    companion object {
        private const val MAX_PERCENTAGE = 100
    }

    override fun onAnnualGrossSalaryChanged(annualGrossSalary: Float?) {
        updateInput { state ->
            state.copy(
                annualGrossSalary = state.annualGrossSalary.copy(
                    text = annualGrossSalary?.toString()
                )
            )
        }
    }

    override fun onFoodCardPerDiemChanged(foodCardPerDiem: Float?) {
        updateInput { state ->
            state.copy(
                foodCardPerDiem = state.foodCardPerDiem.copy(
                    text = foodCardPerDiem?.toString()
                )
            )
        }
    }

    override fun onSavingsPercentageChanged(savingsPercentage: Int?) {
        updateInput { state ->
            val updatedState = state.copy(
                savingsPercentage = state.savingsPercentage.copy(
                    text = savingsPercentage?.toString()
                )
            )

            updatePercentagesError(updatedState)
        }
    }

    override fun onNecessitiesPercentageChanged(necessitiesPercentage: Int?) {
        updateInput { state ->
            val updatedState = state.copy(
                necessitiesPercentage = state.necessitiesPercentage.copy(
                    text = necessitiesPercentage?.toString()
                )
            )

            updatePercentagesError(updatedState)
        }
    }

    override fun onLuxuriesPercentageChanged(luxuriesPercentage: Int?) {
        updateInput { state ->
            val updatedState = state.copy(
                luxuriesPercentage = state.luxuriesPercentage.copy(
                    text = luxuriesPercentage?.toString()
                )
            )

            updatePercentagesError(updatedState)
        }
    }

    override fun onNumberOfDependantsChanged(numberOfDependants: Int?) {
        updateInput { state ->
            state.copy(
                numberOfDependants = state.numberOfDependants.copy(
                    text = numberOfDependants?.toString()
                )
            )
        }
    }

    override fun onSingleIncomeChanged(singleIncome: Boolean) {
        updateInput { state ->
            state.copy(singleIncome = singleIncome)
        }
    }

    override fun onMarriedChanged(married: Boolean) {
        updateInput { state ->
            state.copy(married = married)
        }
    }

    override fun onHandicappedChanged(handicapped: Boolean) {
        updateInput { state ->
            state.copy(handicapped = handicapped)
        }
    }

    /**
     * Validates the income input of the user.
     *
     * @param inputState the current input state of the screen.
     * @return an error message if there's any error, null otherwise.
     */
    override fun validateIncome(
        inputState: EditFinancialProfileInputState
    ): String? {
        val validIncome = inputState.annualGrossSalary.text?.toFloatOrNull() != null
        val validFoodCardPerDiem = inputState.foodCardPerDiem.text?.toFloatOrNull() != null

        return if (!validIncome || !validFoodCardPerDiem) {
            "Please enter a valid income"
        } else {
            null
        }
    }

    /**
     * Validates the percentages input of the user.
     * The sum of the percentages must be less than or equal to 100.
     *
     * @param inputState the current input state of the screen.
     * @return an error message if there's any error, null otherwise.
     */
    override fun validatePercentages(
        inputState: EditFinancialProfileInputState
    ): String? {
        val percentages = listOf(
            inputState.savingsPercentage.text?.toIntOrNull(),
            inputState.necessitiesPercentage.text?.toIntOrNull(),
            inputState.luxuriesPercentage.text?.toIntOrNull()
        )

        if (percentages.mapNotNull { it }.sum() > MAX_PERCENTAGE) {
            return "Total percentage must be less than or equal to $MAX_PERCENTAGE"
        }

        return null
    }

    /**
     * Helper function to update the error message of the percentages input.
     * This is just a boilerplate code to avoid code duplication.
     *
     * @param inputState the current input state of the screen.
     * @return the updated state with the error messages of the percentages input.
     * @see validatePercentages
     */
    private fun updatePercentagesError(inputState: EditFinancialProfileInputState): EditFinancialProfileInputState {
        return inputState.copy(
            savingsPercentage = inputState.savingsPercentage.copy(
                error = validatePercentages(inputState)
            ),
            necessitiesPercentage = inputState.necessitiesPercentage.copy(
                error = validatePercentages(inputState)
            ),
            luxuriesPercentage = inputState.luxuriesPercentage.copy(
                error = validatePercentages(inputState)
            ),
        )
    }

    /**
     * Checks if the save button should be enabled.
     *
     * The button should be enabled if the user has entered valid data.
     *
     * @param inputState the current state of the screen.
     * @return true if the button should be enabled, false otherwise.
     */
    override fun shouldEnableSaveButton(inputState: EditFinancialProfileInputState): Boolean {
        return validatePercentages(inputState).isNullOrEmpty() &&
                validateIncome(inputState).isNullOrEmpty()
    }

    /**
     * Helper function to update the inputState state of the screen.
     *
     * @param update the function to update the content state.
     * @see Content
     */
    override fun updateInput(
        update: (EditFinancialProfileInputState) -> EditFinancialProfileInputState
    ) {
        inputState.update { currentState ->
            update(currentState)
        }
    }
}