package com.eyther.lumbridge.domain.mapper.user

import com.eyther.lumbridge.data.model.UserEntity
import com.eyther.lumbridge.domain.model.locale.InternalLocale
import com.eyther.lumbridge.domain.model.user.UserDomain

fun UserEntity.toDomain() = UserDomain(
    grossSalary = grossSalary,
    locale = InternalLocale.get(countryCode)
)
