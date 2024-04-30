package com.eyther.lumbridge.domain.mapper.user

import com.eyther.lumbridge.data.model.user.UserCached
import com.eyther.lumbridge.domain.model.user.UserDomain

fun UserDomain.toCached() = UserCached(
    countryCode = locale.countryCode,
    annualGrossSalary = annualGrossSalary,
    foodCardPerDiem = foodCardPerDiem
)
