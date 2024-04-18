package com.eyther.lumbridge.mapper.user

import com.eyther.lumbridge.domain.model.user.UserDomain
import com.eyther.lumbridge.model.user.UserUi

fun UserDomain.toUi() = UserUi(
    grossSalary = grossSalary,
    locale = locale
)
