package com.eyther.lumbridge.features.tools.overview.navigation

import androidx.annotation.StringRes
import com.eyther.lumbridge.R
import com.eyther.lumbridge.ui.navigation.NavigationItem

sealed class ToolsNavigationItem(
    route: String,
    @StringRes label: Int
) : NavigationItem(route, label) {

    data object Overview : ToolsNavigationItem(
        route = "tools_overview",
        label = R.string.tools
    )

    sealed interface NetSalary {
        companion object {
            const val HOST_ROUTE = "net_salary"
        }

        data object Input : ToolsNavigationItem(
            "net_salary_input",
            R.string.tools_net_salary_calculator
        )

        data object Result : ToolsNavigationItem(
            "net_salary_result",
            R.string.tools_net_salary_result
        )
    }

    sealed interface Shopping {
        companion object {
            const val HOST_ROUTE = "shopping"
            const val ARG_SHOPPING_LIST_ID = "shoppingListId"
        }

        data object ShoppingList : ToolsNavigationItem(
            route = "shopping_list",
            label = R.string.tools_shopping_list
        )

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

        data object NotesList : ToolsNavigationItem(
            route = "notes_list",
            label = R.string.tools_notes_list
        )

        data object NotesDetails : ToolsNavigationItem(
            route = "notes_details/{$ARG_NOTE_ID}",
            label = R.string.tools_notes_details
        )
    }

    data object CurrencyConverter : ToolsNavigationItem(
        route = "currency_converter",
        label = R.string.tools_currency_converter
    )
}
