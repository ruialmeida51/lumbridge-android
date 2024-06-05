package com.eyther.lumbridge.model.finance

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class MoneyAllocationUi(
    val amount: Float,
    @StringRes val label: Int
): Parcelable
