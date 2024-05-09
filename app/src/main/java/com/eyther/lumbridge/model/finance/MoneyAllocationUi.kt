package com.eyther.lumbridge.model.finance

import androidx.annotation.StringRes

data class MoneyAllocationUi(
    val amount: Float,
    @StringRes val label: Int
)
