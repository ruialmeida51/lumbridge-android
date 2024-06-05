package com.eyther.lumbridge.features.editmortgageprofile.viewmodel.delegate

import com.eyther.lumbridge.R
import com.eyther.lumbridge.domain.time.toLocalDate
import com.eyther.lumbridge.extensions.kotlin.getErrorOrNull
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewState.Content
import com.eyther.lumbridge.features.editmortgageprofile.model.EditMortgageProfileInputState
import com.eyther.lumbridge.model.mortgage.MortgageTypeUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class EditMortgageProfileInputHandler @Inject constructor() : IEditMortgageProfileInputHandler {
    override val inputState = MutableStateFlow(EditMortgageProfileInputState())

    override fun onMortgageAmountChanged(mortgageAmount: Float?) {
        updateInput { state ->
            val loanAmount = mortgageAmount?.toString()

            state.copy(
                loanAmount = state.loanAmount.copy(
                    text = loanAmount,
                    error = loanAmount.getErrorOrNull(
                        R.string.edit_mortgage_profile_invalid_loan_amount
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
                        R.string.edit_mortgage_profile_invalid_euribor
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
                        R.string.edit_mortgage_profile_invalid_spread
                    )
                )
            )
        }
    }

    override fun onFixedInterestRateChanged(fixedInterestRate: Float?) {
        updateInput { state ->
            val fixedInterestRateText = fixedInterestRate?.toString()

            state.copy(
                fixedInterestRate = state.fixedInterestRate.copy(
                    text = fixedInterestRateText,
                    error = fixedInterestRateText.getErrorOrNull(
                        R.string.edit_mortgage_profile_invalid_interest_rate
                    )
                )
            )
        }
    }

    override fun onMortgageTypeChanged(option: Int) {
        updateInput { state ->
            state.copy(
                mortgageChoiceState = state.mortgageChoiceState.copy(
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
                        R.string.edit_mortgage_profile_invalid_start_date
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
                        R.string.edit_mortgage_profile_invalid_end_date
                    )
                )
            )
        }
    }

    override fun validateInput(inputState: EditMortgageProfileInputState): Boolean {
        val isMortgageTypeValid = when (inputState.mortgageChoiceState.selectedTab) {
            MortgageTypeUi.Fixed.ordinal -> {
                inputState.fixedInterestRate.isValid()
            }

            MortgageTypeUi.Variable.ordinal -> {
                inputState.euribor.isValid() && inputState.spread.isValid()
            }

            else -> false
        }

        return inputState.loanAmount.isValid() &&
                inputState.loanAmount.isValid() &&
                inputState.startDate.isValid() &&
                inputState.endDate.isValid() &&
                isMortgageTypeValid
    }

    override fun shouldEnableSaveButton(inputState: EditMortgageProfileInputState): Boolean {
        return validateInput(inputState)
    }

    /**
     * Helper function to update the inputState state of the screen.
     *
     * @param update the function to update the content state.
     * @see Content
     */
    override fun updateInput(
        update: (EditMortgageProfileInputState) -> EditMortgageProfileInputState
    ) {
        inputState.update { currentState ->
            update(currentState)
        }
    }
}
