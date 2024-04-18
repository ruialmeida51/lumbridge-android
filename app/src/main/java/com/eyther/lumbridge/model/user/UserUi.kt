package com.eyther.lumbridge.model.user

import com.eyther.lumbridge.domain.model.locale.InternalLocale

data class UserUi(
    val grossSalary: Float,
    val locale: InternalLocale
)
