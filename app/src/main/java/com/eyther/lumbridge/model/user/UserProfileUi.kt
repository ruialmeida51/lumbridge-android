package com.eyther.lumbridge.model.user

import android.graphics.Bitmap
import com.eyther.lumbridge.domain.model.locale.SupportedLocales

data class UserProfileUi(
    val name: String,
    val email: String,
    val imageBitmap: Bitmap?,
    val locale: SupportedLocales
)
