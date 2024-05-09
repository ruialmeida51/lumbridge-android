package com.eyther.lumbridge.features.overview.model

import androidx.annotation.StringRes
import com.eyther.lumbridge.R

sealed class FinancialOverviewTabItem(@StringRes val label: Int, val ordinal: Int) {
    companion object {
        fun getItems() = listOf(PersonalOverview, MortgageOverview)
    }

    data object PersonalOverview : FinancialOverviewTabItem(
        label = R.string.financial_overview_personal,
        ordinal = 0
    )

    data object MortgageOverview : FinancialOverviewTabItem(
        label = R.string.financial_overview_mortgage,
        ordinal = 1
    )
}
