package com.eyther.lumbridge.data.model.user.local

import android.graphics.Bitmap

data class UserProfileCached(
    val name: String,
    val email: String,
    val imageBitmap: Bitmap?,
    val countryCode: String
)
