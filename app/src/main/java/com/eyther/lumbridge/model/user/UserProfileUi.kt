package com.eyther.lumbridge.model.user

import com.eyther.lumbridge.domain.model.locale.SupportedLocales

data class UserProfileUi(
    val name: String,
    val email: String,
    val locale: SupportedLocales
)
