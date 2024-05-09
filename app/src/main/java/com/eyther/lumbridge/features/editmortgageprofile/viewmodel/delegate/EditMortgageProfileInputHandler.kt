package com.eyther.lumbridge.features.editmortgageprofile.viewmodel.delegate

import androidx.annotation.StringRes
import com.eyther.lumbridge.R
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewState.Content
import com.eyther.lumbridge.features.editmortgageprofile.model.EditMortgageProfileInputState
import com.eyther.lumbridge.model.mortgage.MortgageTypeUi
import com.eyther.lumbridge.ui.common.composables.model.TextInputState
import com.eyther.lumbridge.ui.common.model.text.TextResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class EditMortgageProfileInputHandler @Inject constructor() : IEditMortgageProfileInputHandler {
    override val inputState = MutableStateFlow(EditMortgageProfileInputState())

    override fun onMortgageAmountChanged(mortgageAmount: Float?) {
        updateInput { state ->
            state.copy(
                loanAmount = state.loanAmount.copy(
                    text = mortgageAmount?.toString()
                )
            )
        }
    }

    override fun onEuriborRateChanged(euriborRate: Float?) {
        updateInput { state ->
            state.copy(
                euribor = state.euribor.copy(
                    text = euriborRate?.toString()
                )
            )
        }
    }

    override fun onSpreadChanged(spread: Float?) {
        updateInput { state ->
            state.copy(
                spread = state.spread.copy(
                    text = spread?.toString()
                )
            )
        }
    }

    override fun onFixedInterestRateChanged(fixedInterestRate: Float?) {
        updateInput { state ->
            state.copy(
                fixedInterestRate = state.fixedInterestRate.copy(
                    text = fixedInterestRate?.toString()
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
            state.copy(
                monthsLeft = state.monthsLeft.copy(
                    text = monthsLeft?.toString()
                )
            )
        }
    }

    override fun validateInput(inputState: EditMortgageProfileInputState): Boolean {
        updateInput {
            it.copy(
                loanAmount = updateErrorOrNull(
                    inputState.loanAmount,
                    R.string.edit_mortgage_profile_invalid_loan_amount
                )
            )
        }

        updateInput { state ->
            state.copy(
                monthsLeft = updateErrorOrNull(
                    state.monthsLeft,
                    R.string.edit_mortgage_profile_invalid_months_left
                )
            )
        }

        when (inputState.mortgageType) {
            MortgageTypeUi.Fixed -> {
                updateInput { state ->
                    state.copy(
                        fixedInterestRate = updateErrorOrNull(
                            inputState.fixedInterestRate,
                            R.string.edit_mortgage_profile_invalid_interest_rate
                        )
                    )
                }
            }

            MortgageTypeUi.Variable -> {
                updateInput {
                    it.copy(
                        euribor = updateErrorOrNull(
                            inputState.euribor,
                            R.string.edit_mortgage_profile_invalid_euribor
                        )
                    )
                }

                updateInput {
                    it.copy(
                        spread = updateErrorOrNull(
                            inputState.spread,
                            R.string.edit_mortgage_profile_invalid_spread
                        )
                    )
                }
            }

            null -> Unit
        }

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

    private fun updateErrorOrNull(
        textInputState: TextInputState,
        @StringRes errorRes: Int
    ) = if (textInputState.text.isNullOrEmpty()) {
        textInputState.copy(error = TextResource.Resource(resId = errorRes))
    } else {
        textInputState.copy(error = null)
    }
}
