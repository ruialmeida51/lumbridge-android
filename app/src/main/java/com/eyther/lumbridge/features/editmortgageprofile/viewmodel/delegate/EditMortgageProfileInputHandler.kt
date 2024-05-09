package com.eyther.lumbridge.features.editmortgageprofile.viewmodel.delegate

import androidx.annotation.StringRes
import com.eyther.lumbridge.R
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewState.Content
import com.eyther.lumbridge.features.editmortgageprofile.model.EditMortgageProfileInputState
import com.eyther.lumbridge.model.mortgage.MortgageTypeUi
import com.eyther.lumbridge.ui.common.model.text.TextResource
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

    override fun onMortgageTypeChanged(mortgageType: MortgageTypeUi?) {
        updateInput { state ->
            state.copy(mortgageType = mortgageType)
        }
    }

    override fun onMonthsLeftChanged(monthsLeft: Int?) {
        updateInput { state ->
            val monthsLeftText = monthsLeft?.toString()

            state.copy(
                monthsLeft = state.monthsLeft.copy(
                    text = monthsLeftText,
                    error = monthsLeftText.getErrorOrNull(
                        R.string.edit_mortgage_profile_invalid_months_left
                    )
                )
            )
        }
    }

    override fun validateInput(inputState: EditMortgageProfileInputState): Boolean {
        val isMortgageTypeValid = when (inputState.mortgageType) {
            MortgageTypeUi.Fixed -> {
                inputState.fixedInterestRate.error == null
            }

            MortgageTypeUi.Variable -> {
                inputState.euribor.error == null && inputState.spread.error == null
            }

            else -> false
        }

        return inputState.loanAmount.error == null &&
                inputState.monthsLeft.error == null &&
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

    /**
     * Helper function to get the error message of the text input.
     * This is just a boilerplate code to avoid code duplication.
     * @param errorRes the error message resource id.
     * @return the updated state with the error message of the text input.
     * @see validateInput
     */
    private fun String?.getErrorOrNull(@StringRes errorRes: Int) = if (isNullOrEmpty()) {
        TextResource.Resource(resId = errorRes)
    } else {
        null
    }
}
