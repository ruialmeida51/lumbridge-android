package com.eyther.lumbridge.domain.mapper.user

import com.eyther.lumbridge.data.model.UserEntity
import com.eyther.lumbridge.domain.model.user.UserDomain

fun UserDomain.toCached() = UserEntity(
    countryCode = locale.countryCode,
    grossSalary = grossSalary
)
