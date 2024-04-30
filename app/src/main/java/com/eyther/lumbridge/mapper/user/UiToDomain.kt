package com.eyther.lumbridge.mapper.user

import com.eyther.lumbridge.domain.model.user.UserDomain
import com.eyther.lumbridge.model.user.UserUi

fun UserUi.toDomain() = UserDomain(
    locale = locale,
    annualGrossSalary = annualGrossSalary,
    foodCardPerDiem = foodCardPerDiem
)
