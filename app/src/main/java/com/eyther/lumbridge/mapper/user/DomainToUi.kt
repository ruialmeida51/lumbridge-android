package com.eyther.lumbridge.mapper.user

import com.eyther.lumbridge.domain.model.user.UserDomain
import com.eyther.lumbridge.model.user.UserUi

fun UserDomain.toUi() = UserUi(
    annualGrossSalary = annualGrossSalary,
    foodCardPerDiem = foodCardPerDiem,
    locale = locale
)
