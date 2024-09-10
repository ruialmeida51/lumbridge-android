package com.eyther.lumbridge.features.profile.editprofile.model

import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.ui.common.composables.model.input.TextInputState

data class EditProfileScreenInputState(
    val name: TextInputState = TextInputState(),
    val email: TextInputState = TextInputState(),
    val locale: SupportedLocales = SupportedLocales.PORTUGAL
)
