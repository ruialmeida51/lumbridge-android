package com.eyther.lumbridge.features.editmortgageprofile.model

import com.eyther.lumbridge.model.mortgage.MortgageTypeUi
import com.eyther.lumbridge.ui.common.composables.model.TextInputState

data class EditMortgageProfileInputState(
    val euribor: TextInputState = TextInputState(),
    val spread: TextInputState = TextInputState(),
    val loanAmount: TextInputState = TextInputState(),
    val monthsLeft: TextInputState = TextInputState(),
    val fixedInterestRate: TextInputState = TextInputState(),
    val mortgageType: MortgageTypeUi? = null
)
