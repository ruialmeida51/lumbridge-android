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
                Personal.SavingsCalculator,
                Personal.CurrencyConverter
            ),
            R.string.tools_mortgage_calculator to listOf(
                Mortgage.MortgageCalculator
            ),
            R.string.tools_company_tools to listOf(
                Company.CostToCompanyCalculator
            )
        )
    }

    sealed interface Personal {
        data object NetSalaryCalculator : ToolItem(
            text = R.string.tools_net_salary_calculator,
            icon = R.drawable.ic_money
        )

        data object SavingsCalculator : ToolItem(
            text = R.string.tools_savings_calculator,
            icon = R.drawable.ic_savings
        )

        data object CurrencyConverter : ToolItem(
            text = R.string.tools_currency_converter,
            icon = R.drawable.ic_currency_exchange
        )
    }

    sealed class Mortgage {
        data object MortgageCalculator : ToolItem(
            text = R.string.tools_mortgage_calculator,
            icon = R.drawable.ic_bank
        )
    }

    sealed class Company {
        data object CostToCompanyCalculator : ToolItem(
            text = R.string.tools_ctc_calculator_long,
            icon = R.drawable.ic_work
        )
    }
}
