package com.eyther.lumbridge.features.editfinancialprofile.model

import com.eyther.lumbridge.ui.common.composables.model.input.ChoiceTabState
import com.eyther.lumbridge.ui.common.composables.model.input.TextInputState

data class EditFinancialProfileInputState(
    val annualGrossSalary: TextInputState = TextInputState(),
    val monthlyGrossSalary: TextInputState = TextInputState(),
    val foodCardPerDiem: TextInputState = TextInputState(),
    val savingsPercentage: TextInputState = TextInputState(),
    val necessitiesPercentage: TextInputState = TextInputState(),
    val luxuriesPercentage: TextInputState = TextInputState(),
    val numberOfDependants: TextInputState = TextInputState(),
    val salaryInputChoiceState: ChoiceTabState = ChoiceTabState(),
    val singleIncome: Boolean = false,
    val married: Boolean = false,
    val handicapped: Boolean = false
)
