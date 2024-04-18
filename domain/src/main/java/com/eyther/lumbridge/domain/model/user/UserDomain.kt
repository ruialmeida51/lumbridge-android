package com.eyther.lumbridge.domain.model.user

import com.eyther.lumbridge.domain.model.locale.InternalLocale

data class UserDomain(
    val grossSalary: Float,
    val locale: InternalLocale
)
