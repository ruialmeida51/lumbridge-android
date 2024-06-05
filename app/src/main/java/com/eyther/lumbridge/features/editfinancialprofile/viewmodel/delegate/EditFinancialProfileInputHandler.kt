package com.eyther.lumbridge.features.editfinancialprofile.viewmodel.delegate

import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.getErrorOrNull
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileInputState
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewState.Content
import com.eyther.lumbridge.model.finance.SalaryInputTypeUi
import com.eyther.lumbridge.ui.common.model.text.TextResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class EditFinancialProfileInputHandler @Inject constructor() : IEditFinancialProfileInputHandler {

    override val inputState = MutableStateFlow(EditFinancialProfileInputState())

    companion object {
        private const val MAX_PERCENTAGE = 100
    }

    override fun onSalaryInputTypeChanged(option: Int) {
        updateInput { state ->
            state.copy(
                salaryInputChoiceState = state.salaryInputChoiceState.copy(
                    selectedTab = option
                )
            )
        }
    }

    override fun onAnnualGrossSalaryChanged(annualGrossSalary: Float?) {
        updateInput { state ->
            val annualGrossSalaryText = annualGrossSalary?.toString()

            state.copy(
                annualGrossSalary = state.annualGrossSalary.copy(
                    text = annualGrossSalaryText,
                    error = annualGrossSalaryText.getErrorOrNull(R.string.edit_financial_profile_invalid_income)
                )
            )
        }
    }

    override fun onMonthlyGrossSalaryChanged(monthlyGrossSalary: Float?) {
        updateInput { state ->
            val monthlyGrossSalaryText = monthlyGrossSalary?.toString()

            state.copy(
                monthlyGrossSalary = state.monthlyGrossSalary.copy(
                    text = monthlyGrossSalaryText,
                    error = monthlyGrossSalaryText.getErrorOrNull(R.string.edit_financial_profile_invalid_income)
                )
            )
        }
    }

    override fun onFoodCardPerDiemChanged(foodCardPerDiem: Float?) {
        updateInput { state ->
            val foodCardPerDiemText = foodCardPerDiem?.toString()

            state.copy(
                foodCardPerDiem = state.foodCardPerDiem.copy(
                    text = foodCardPerDiemText,
                    error = foodCardPerDiemText.getErrorOrNull(R.string.edit_financial_profile_invalid_food_card)
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
     * Validates the percentages input of the user.
     * The sum of the percentages must be less than or equal to 100.
     *
     * @param inputState the current input state of the screen.
     * @return an error message if there's any error, null otherwise.
     */
    override fun validatePercentages(
        inputState: EditFinancialProfileInputState
    ): TextResource? {
        val percentages = listOf(
            inputState.savingsPercentage.text?.toIntOrNull(),
            inputState.necessitiesPercentage.text?.toIntOrNull(),
            inputState.luxuriesPercentage.text?.toIntOrNull()
        )

        if (percentages.mapNotNull { it }.sum() > MAX_PERCENTAGE) {
            return TextResource.Resource(R.string.edit_financial_profile_invalid_allocation)
        }

        return null
    }

    /**
     * Validates the salary input of the user.
     * The salary input must be greater than 0.
     *
     * @param inputState the current input state of the screen.
     * @return true if the salary input is valid, false otherwise.
     */
    private fun validateSalaryInput(inputState: EditFinancialProfileInputState): Boolean {
        return when (inputState.salaryInputChoiceState.selectedTab) {
            SalaryInputTypeUi.Monthly.ordinal -> inputState.monthlyGrossSalary.isValid()
            SalaryInputTypeUi.Annually.ordinal -> inputState.annualGrossSalary.isValid()
            else -> false
        }
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
        return validatePercentages(inputState) == null &&
                validateSalaryInput(inputState) &&
                inputState.foodCardPerDiem.isValid()
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
}