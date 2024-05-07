package com.eyther.lumbridge.features.tools.overview.model

import androidx.annotation.DrawableRes
import com.eyther.lumbridge.R

sealed class ToolItem(
    open val title: String,
    val text: String,
    @DrawableRes val icon: Int
) {
    companion object {
        fun getItems() = listOf(
            Personal.NetSalaryCalculator,
            Personal.SavingsCalculator,
            Mortgage.MortgageCalculator,
            Company.CostToCompanyCalculator
        )
    }

    sealed interface Personal {
        companion object {
            const val TITLE = "Personal Tools"
        }

        data object NetSalaryCalculator : ToolItem(
            text = "Net Salary Calculator",
            icon = R.drawable.ic_money,
            title = TITLE
        )

        data object SavingsCalculator : ToolItem(
            text = "Savings Calculator",
            icon = R.drawable.ic_savings,
            title = TITLE
        )
    }

    sealed class Mortgage {
        companion object {
            const val TITLE = "Mortgage Tools"
        }

        data object MortgageCalculator : ToolItem(
            text = "Mortgage Calculator",
            icon = R.drawable.ic_bank,
            title = TITLE
        )
    }

    sealed class Company {
        companion object {
            const val TITLE = "Company Tools"
        }

        data object CostToCompanyCalculator : ToolItem(
            text = "Cost to Company Calculator",
            icon = R.drawable.ic_work,
            title = TITLE
        )
    }
}
