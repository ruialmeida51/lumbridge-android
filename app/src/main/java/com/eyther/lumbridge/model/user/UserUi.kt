package com.eyther.lumbridge.model.user

import com.eyther.lumbridge.domain.model.locale.SupportedLocales

data class UserUi(
    val annualGrossSalary: Float,
    val foodCardPerDiem: Float,
    val locale: SupportedLocales
)
