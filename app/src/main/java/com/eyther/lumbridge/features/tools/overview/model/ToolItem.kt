package com.eyther.lumbridge.features.tools.overview.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.eyther.lumbridge.R

sealed class ToolItem(
    @StringRes val text: Int,
    @DrawableRes val icon: Int
) {
    companion object {
        fun getItems() = mapOf(
            R.string.tools_personal_tools to listOf(
                Personal.NetSalaryCalculator,
                Personal.CurrencyConverter
            )
        )
    }

    sealed interface Personal {
        data object NetSalaryCalculator : ToolItem(
            text = R.string.tools_net_salary_calculator,
            icon = R.drawable.ic_money
        )

        data object CurrencyConverter : ToolItem(
            text = R.string.tools_currency_converter,
            icon = R.drawable.ic_currency_exchange
        )
    }
}
