package com.eyther.lumbridge.domain.mapper.user

import com.eyther.lumbridge.data.model.user.UserFinancialsCached
import com.eyther.lumbridge.data.model.user.UserProfileCached
import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain
import com.eyther.lumbridge.domain.model.user.UserProfileDomain

fun UserProfileDomain.toCached() = UserProfileCached(
    countryCode = locale.countryCode,
    name = name,
    email = email
)

fun UserFinancialsDomain.toCached() = UserFinancialsCached(
    annualGrossSalary = annualGrossSalary,
    foodCardPerDiem = foodCardPerDiem
)
