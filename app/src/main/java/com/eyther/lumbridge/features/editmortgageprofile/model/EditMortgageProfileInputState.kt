package com.eyther.lumbridge.features.editmortgageprofile.model

import com.eyther.lumbridge.ui.common.composables.model.input.ChoiceTabState
import com.eyther.lumbridge.ui.common.composables.model.input.DateInputState
import com.eyther.lumbridge.ui.common.composables.model.input.TextInputState

data class EditMortgageProfileInputState(
    val euribor: TextInputState = TextInputState(),
    val spread: TextInputState = TextInputState(),
    val loanAmount: TextInputState = TextInputState(),
    val fixedInterestRate: TextInputState = TextInputState(),
    val startDate: DateInputState = DateInputState(),
    val endDate: DateInputState = DateInputState(),
    val mortgageChoiceState: ChoiceTabState = ChoiceTabState()
)
