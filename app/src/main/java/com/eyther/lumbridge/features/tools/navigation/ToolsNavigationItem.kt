package com.eyther.lumbridge.features.tools.navigation

import androidx.annotation.StringRes
import com.eyther.lumbridge.R
import com.eyther.lumbridge.ui.navigation.NavigationItem
import kotlinx.serialization.Serializable

@Serializable
sealed class ToolsNavigationItem(
    override val route: String,
    @StringRes override val label: Int
) : NavigationItem() {

    @Serializable
    data object Overview : ToolsNavigationItem(
        route = "tools_overview",
        label = R.string.tools
    )

    sealed interface Shopping {
        companion object {
            const val HOST_ROUTE = "shopping"
            const val ARG_SHOPPING_LIST_ID = "shoppingListId"
        }

        @Serializable
        data object ShoppingList : ToolsNavigationItem(
            route = "shopping_list",
            label = R.string.tools_shopping_list
        )

        @Serializable
        data object ShoppingListDetails : ToolsNavigationItem(
            route = "shopping_list_details/{$ARG_SHOPPING_LIST_ID}",
            label = R.string.tools_shopping_list_details
        )
    }

    sealed interface Notes {
        companion object {
            const val HOST_ROUTE = "notes"
            const val ARG_NOTE_ID = "noteId"
        }

        @Serializable
        data object NotesList : ToolsNavigationItem(
            route = "notes_list",
            label = R.string.tools_notes_list
        )

        @Serializable
        data object NotesDetails : ToolsNavigationItem(
            route = "notes_details/{$ARG_NOTE_ID}",
            label = R.string.tools_notes_details
        )
    }

    sealed interface RecurringPayments {
        companion object {
            const val HOST_ROUTE = "recurring_payments"
            const val ARG_RECURRING_PAYMENT_ID = "recurringPaymentId"
        }

        @Serializable
        data object Overview : ToolsNavigationItem(
            route = "recurring_payments_overview",
            label = R.string.recurring_payments_overview
        )

        @Serializable
        data object Edit : ToolsNavigationItem(
            route = "edit_recurring_payment/{$ARG_RECURRING_PAYMENT_ID}",
            label = R.string.recurring_payments_edit
        )
    }

    sealed interface NetSalary {
        companion object {
            const val HOST_ROUTE = "net_salary"
        }

        @Serializable
        data object Input : ToolsNavigationItem(
            "net_salary_input",
            R.string.tools_net_salary_calculator
        )

        @Serializable
        data object Result : ToolsNavigationItem(
            "net_salary_result",
            R.string.tools_net_salary_result
        )
    }

    @Serializable
    data object CurrencyConverter : ToolsNavigationItem(
        route = "currency_converter",
        label = R.string.tools_currency_converter
    )
}
