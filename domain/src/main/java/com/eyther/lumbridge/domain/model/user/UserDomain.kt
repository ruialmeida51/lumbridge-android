package com.eyther.lumbridge.domain.model.user

import com.eyther.lumbridge.domain.model.locale.SupportedLocales

data class UserDomain(
    val annualGrossSalary: Float,
    val foodCardPerDiem: Float,
    val locale: SupportedLocales
)
