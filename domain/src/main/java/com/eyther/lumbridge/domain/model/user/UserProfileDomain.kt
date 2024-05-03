package com.eyther.lumbridge.domain.model.user

import com.eyther.lumbridge.domain.model.locale.SupportedLocales

data class UserProfileDomain(
    val name: String,
    val email: String,
    val locale: SupportedLocales
)
