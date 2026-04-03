package com.eyther.lumbridge.features.profile.navigation

import androidx.annotation.StringRes
import com.eyther.lumbridge.R
import com.eyther.lumbridge.features.overview.navigation.OverviewNavigationItem.Loan.Companion.ARG_LOAN_ID
import com.eyther.lumbridge.ui.navigation.NavigationItem
import kotlinx.serialization.Serializable

@Serializable
sealed class ProfileNavigationItem(
    override val route: String,
    @StringRes override val label: Int
) : NavigationItem() {

    @Serializable
    data object ProfileOverview : ProfileNavigationItem(
        route = "profile_overview",
        label = R.string.profile
    )

    @Serializable
    data object EditProfile : ProfileNavigationItem(
        route = "edit_profile",
        label = R.string.edit_profile
    )

    @Serializable
    data object Settings : ProfileNavigationItem(
        route = "settings",
        label = R.string.settings
    )

    @Serializable
    data object EditFinancialProfile : ProfileNavigationItem(
        route = "edit_financial_profile",
        label = R.string.edit_financial_profile
    )

    sealed interface Loans {
        companion object {
            const val HOST_ROUTE = "loans"
        }

        @Serializable
        data object List : ProfileNavigationItem(
            route = "loans_list",
            label = R.string.profile_edit_loans
        )

        @Serializable
        data object Edit : ProfileNavigationItem(
            route = "loans_edit/{$ARG_LOAN_ID}",
            label = R.string.edit_loan
        )
    }
}
