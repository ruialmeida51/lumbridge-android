package com.eyther.lumbridge.features.expenses.model.overview

import androidx.annotation.StringRes
import com.eyther.lumbridge.R

/**
 * Represents how we can sort the expenses overview.
 *
 * We can sort by:
 * - Date, ascending: The oldest expenses will be shown first
 * - Date, descending: The newest expenses will be shown first
 * - Spent, ascending: The months with the lowest amount will be shown first
 * - Spent, descending: The months with the highest amount will be shown first
 *
 * @see ExpensesOverviewScreenViewState
 */
sealed class ExpensesOverviewSortBy(val ordinal: Int) {
    companion object {
        data class DisplaySortBy(
            @StringRes val nameRes: Int,
            val ordinal: Int
        )

        fun get(): List<DisplaySortBy> = listOf(
            DisplaySortBy(nameRes = R.string.sort_by_date_ascending, ordinal = SORT_BY_DATE_ASCENDING_ORDINAL),
            DisplaySortBy(nameRes = R.string.sort_by_date_descending, ordinal = SORT_BY_DATE_DESCENDING_ORDINAL),
            DisplaySortBy(nameRes = R.string.sort_by_spent_ascending, ordinal = SORT_BY_SPENT_ASCENDING_ORDINAL),
            DisplaySortBy(nameRes = R.string.sort_by_spent_descending, ordinal = SORT_BY_SPENT_DESCENDING_ORDINAL)
        )

        fun of(ordinal: Int): ExpensesOverviewSortBy {
            return when (ordinal) {
                SORT_BY_DATE_ASCENDING_ORDINAL -> DateAscending
                SORT_BY_DATE_DESCENDING_ORDINAL -> DateDescending
                SORT_BY_SPENT_ASCENDING_ORDINAL -> SpentAscending
                SORT_BY_SPENT_DESCENDING_ORDINAL -> SpentDescending
                else -> throw IllegalArgumentException("💥 Unknown ordinal: $ordinal")
            }
        }

        fun ExpensesOverviewSortBy.toDisplaySortBy(): DisplaySortBy {
            return get().first { it.ordinal == ordinal }
        }

        private const val SORT_BY_DATE_ASCENDING_ORDINAL = 0
        private const val SORT_BY_DATE_DESCENDING_ORDINAL = 1
        private const val SORT_BY_SPENT_ASCENDING_ORDINAL = 2
        private const val SORT_BY_SPENT_DESCENDING_ORDINAL = 3
    }

    data object DateAscending : ExpensesOverviewSortBy(SORT_BY_DATE_ASCENDING_ORDINAL)
    data object DateDescending : ExpensesOverviewSortBy(SORT_BY_DATE_DESCENDING_ORDINAL)
    data object SpentAscending : ExpensesOverviewSortBy(SORT_BY_SPENT_ASCENDING_ORDINAL)
    data object SpentDescending : ExpensesOverviewSortBy(SORT_BY_SPENT_DESCENDING_ORDINAL)
}
