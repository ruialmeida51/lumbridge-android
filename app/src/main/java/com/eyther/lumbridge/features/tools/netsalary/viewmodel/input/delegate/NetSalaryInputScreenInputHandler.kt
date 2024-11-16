package com.eyther.lumbridge.features.tools.netsalary.viewmodel.input.delegate

import com.eyther.lumbridge.R
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.extensions.kotlin.getErrorOrNull
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewState.Content
import com.eyther.lumbridge.features.tools.netsalary.model.input.NetSalaryInputState
import com.eyther.lumbridge.model.finance.DuodecimosTypeUi
import com.eyther.lumbridge.model.finance.SalaryInputTypeUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class NetSalaryInputScreenInputHandler @Inject constructor() : INetSalaryInputScreenInputHandler {

    override val inputState = MutableStateFlow(NetSalaryInputState())

    override fun onSalaryInputTypeChanged(option: Int) {
        updateInput { state ->
            state.copy(
                salaryInputChoiceState = state.salaryInputChoiceState.copy(
                    selectedTab = option
                )
            )
        }
    }

    override fun onDuodecimosTypeChanged(option: Int) {
        updateInput { state ->
            state.copy(
                duodecimosTypeUi = DuodecimosTypeUi.fromOrdinal(option)
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

    override fun onLocaleChanged(countryCode: String) {
        updateInput { state ->
            state.copy(locale = SupportedLocales.get(countryCode))
        }
    }

    /**
     * Validates the salary input of the user.
     * The salary input must be greater than 0.
     *
     * @param inputState the current input state of the screen.
     * @return true if the salary input is valid, false otherwise.
     */
    private fun validateSalaryInput(inputState: NetSalaryInputState): Boolean {
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
    override fun shouldEnableSaveButton(inputState: NetSalaryInputState): Boolean {
        return validateSalaryInput(inputState) && inputState.foodCardPerDiem.isValid()
    }

    /**
     * Helper function to update the inputState state of the screen.
     *
     * @param update the function to update the content state.
     * @see Content
     */
    override fun updateInput(
        update: (NetSalaryInputState) -> NetSalaryInputState
    ) {
        inputState.update { currentState ->
            update(currentState)
        }
    }
}
