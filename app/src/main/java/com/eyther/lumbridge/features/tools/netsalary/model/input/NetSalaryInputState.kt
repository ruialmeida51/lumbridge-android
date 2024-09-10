package com.eyther.lumbridge.features.tools.netsalary.model.input

import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.ui.common.composables.model.input.ChoiceTabState
import com.eyther.lumbridge.ui.common.composables.model.input.TextInputState

data class NetSalaryInputState(
    val annualGrossSalary: TextInputState = TextInputState(),
    val monthlyGrossSalary: TextInputState = TextInputState(),
    val foodCardPerDiem: TextInputState = TextInputState(),
    val numberOfDependants: TextInputState = TextInputState(),
    val salaryInputChoiceState: ChoiceTabState = ChoiceTabState(),
    val singleIncome: Boolean = false,
    val married: Boolean = false,
    val handicapped: Boolean = false,
    val locale: SupportedLocales = SupportedLocales.PORTUGAL
)
