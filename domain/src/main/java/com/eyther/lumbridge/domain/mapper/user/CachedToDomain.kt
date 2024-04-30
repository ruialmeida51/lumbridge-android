package com.eyther.lumbridge.domain.mapper.user

import com.eyther.lumbridge.data.model.user.UserCached
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.domain.model.user.UserDomain

fun UserCached.toDomain() = UserDomain(
    annualGrossSalary = annualGrossSalary,
    foodCardPerDiem = foodCardPerDiem,
    locale = SupportedLocales.get(countryCode)
)
