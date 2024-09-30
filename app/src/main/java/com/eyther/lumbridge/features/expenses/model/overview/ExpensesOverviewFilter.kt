package com.eyther.lumbridge.features.expenses.model.overview

import androidx.annotation.StringRes
import com.eyther.lumbridge.R
import java.time.Month
import java.time.Year

/**
 * Represents the filter for the expenses overview.
 *
 * We can filter by:
 *
 * - None: No filter will be applied
 * - Date range: The expenses will be shown only for the selected date range
 * - Starting from: The expenses will be shown starting from the selected date
 * - Up to: The expenses will be shown up to the selected date
 *
 * @see ExpensesOverviewScreenViewState
 */
sealed class ExpensesOverviewFilter(
    val ordinal: Int
) {
    companion object {
        const val FILTER_NONE_ORDINAL = 0
        const val FILTER_START_FROM_ORDINAL = 1
        const val FILTER_UP_TO_ORDINAL = 2
        const val FILTER_DATE_RANGE_ORDINAL = 3

        data class DisplayFilter(
            @StringRes val nameRes: Int,
            val ordinal: Int
        )

        fun get(): List<DisplayFilter> = listOf(
            DisplayFilter(nameRes = R.string.filter_none, ordinal = FILTER_NONE_ORDINAL),
            DisplayFilter(nameRes = R.string.filter_date_start_from, ordinal = FILTER_START_FROM_ORDINAL),
            DisplayFilter(nameRes = R.string.filter_date_up_to, ordinal = FILTER_UP_TO_ORDINAL),
            DisplayFilter(nameRes = R.string.filter_date_range, ordinal = FILTER_DATE_RANGE_ORDINAL)
        )
        
        fun of(
            ordinal: Int,
            startYear: Int? = null,
            startMonth: Int? = null,
            endYear: Int? = null,
            endMonth: Int? = null
        ): ExpensesOverviewFilter {
            return when (ordinal) {
                FILTER_NONE_ORDINAL -> None
                FILTER_START_FROM_ORDINAL -> StartingFrom(
                    year = startYear.toYear(),
                    month = startMonth.toMonth()
                )
                FILTER_UP_TO_ORDINAL -> UpTo(
                    year = endYear.toYear(),
                    month = endMonth.toMonth()
                )
                FILTER_DATE_RANGE_ORDINAL -> DateRange(
                    startYear = startYear.toYear(),
                    startMonth = startMonth.toMonth(),
                    endYear = endYear.toYear(),
                    endMonth = endMonth.toMonth()
                )
                else -> throw IllegalArgumentException("💥 Unknown ordinal: $ordinal")
            }
        }

        fun ExpensesOverviewFilter.toDisplayFilter(): DisplayFilter {
            return get().first { it.ordinal == ordinal }
        }

        private fun Int?.toYear() = checkNotNull(this).let { Year.of(it) }
        private fun Int?.toMonth() = checkNotNull(this).let { Month.of(it) }
    }

    data object None : 
        ExpensesOverviewFilter(ordinal = FILTER_NONE_ORDINAL)

    data class StartingFrom(
        val year: Year,
        val month: Month
    ) : ExpensesOverviewFilter(ordinal = FILTER_START_FROM_ORDINAL)

    data class UpTo(
        val year: Year,
        val month: Month
    ) : ExpensesOverviewFilter(ordinal = FILTER_UP_TO_ORDINAL)

    data class DateRange(
        val startYear: Year,
        val startMonth: Month,
        val endYear: Year,
        val endMonth: Month
    ) : ExpensesOverviewFilter(ordinal = FILTER_DATE_RANGE_ORDINAL)
}
