package com.eyther.lumbridge.mapper.user

import com.eyther.lumbridge.domain.model.user.UserDomain
import com.eyther.lumbridge.model.user.UserUi

fun UserUi.toDomain() = UserDomain(
    grossSalary = grossSalary,
    locale = locale
)
