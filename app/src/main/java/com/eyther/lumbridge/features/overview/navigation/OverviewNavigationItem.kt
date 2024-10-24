package com.eyther.lumbridge.features.overview.navigation

import androidx.annotation.StringRes
import com.eyther.lumbridge.R
import com.eyther.lumbridge.ui.navigation.NavigationItem

sealed class OverviewNavigationItem(
    route: String,
    @StringRes label: Int
) : NavigationItem(route, label) {

    data object Breakdown : OverviewNavigationItem(
        route = "financial_overview",
        label = R.string.financial_overview
    )

    sealed interface FinancialProfile {
        companion object {
            const val HOST_ROUTE = "salary"
        }

        data object Details : OverviewNavigationItem(
            route = "financial_profile_overview",
            label = R.string.breakdown_salary_title
        )

        data object Edit : OverviewNavigationItem(
            route = "financial_profile_edit",
            label = R.string.edit_financial_profile
        )
    }

    sealed interface Loan {
        companion object {
            const val HOST_ROUTE = "loan"
            const val ARG_LOAN_ID = "loanId"
        }

        data object Details : OverviewNavigationItem(
            route = "loan_details/{$ARG_LOAN_ID}",
            label = R.string.edit_loan
        )

        data object Edit : OverviewNavigationItem(
            route = "edit_loan/{$ARG_LOAN_ID}",
            label = R.string.edit_loan
        )
    }
}
