package com.eyther.lumbridge.mapper.user

import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain
import com.eyther.lumbridge.domain.model.user.UserProfileDomain
import com.eyther.lumbridge.model.user.UserFinancialsUi
import com.eyther.lumbridge.model.user.UserProfileUi

fun UserProfileUi.toDomain() = UserProfileDomain(
    locale = locale,
    name = name,
    email = email
)

fun UserFinancialsUi.toDomain() = UserFinancialsDomain(
    annualGrossSalary = annualGrossSalary,
    foodCardPerDiem = foodCardPerDiem
)
