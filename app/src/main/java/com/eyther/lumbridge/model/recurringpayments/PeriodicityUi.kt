package com.eyther.lumbridge.model.recurringpayments

import com.eyther.lumbridge.R
import com.eyther.lumbridge.ui.common.model.text.TextResource
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

sealed class PeriodicityUi(
    open val nextDueDate: LocalDate? = null,
    open val ordinal: Int
): Comparable<PeriodicityUi> {

    companion object {
        fun getDefaults() = listOf(
            EveryXDays(LocalDate.now(), 1),
            EveryXWeeks(LocalDate.now(), 1, DayOfWeek.MONDAY),
            EveryXMonths(LocalDate.now(), 1, 1),
            EveryXYears(LocalDate.now(), 1, Month.JANUARY)
        )

        fun defaultFromOrdinal(ordinal: Int) = getDefaults().first { it.ordinal == ordinal }
    }

    abstract fun getPeriodicityHumanReadable(locale: Locale): TextResource
    abstract fun getPeriodicitySelectionHumanReadable(locale: Locale): TextResource

    data class EveryXDays(
        override val nextDueDate: LocalDate? = null,
        val numOfDays: Int
    ) : PeriodicityUi(nextDueDate = nextDueDate, ordinal = 0) {
        override fun getPeriodicityHumanReadable(locale: Locale) = TextResource.PluralResource(
            resId = R.plurals.periodicity_every_x_days,
            quantity = numOfDays,
            args = listOf(numOfDays)
        )

        override fun getPeriodicitySelectionHumanReadable(locale: Locale): TextResource {
            return TextResource.ResourceWithArgs(
                resId = R.string.periodicity_every_x_days_selection,
                args = listOf(numOfDays)
            )
        }

        override fun compareTo(other: PeriodicityUi): Int {
            return when (other) {
                is EveryXDays -> numOfDays.compareTo(other.numOfDays)
                else -> -1
            }
        }
    }

    data class EveryXWeeks(
        override val nextDueDate: LocalDate? = null,
        val numOfWeeks: Int,
        val dayOfWeek: DayOfWeek
    ) : PeriodicityUi(nextDueDate = nextDueDate, ordinal = 1) {
        override fun getPeriodicityHumanReadable(locale: Locale) = TextResource.PluralResource(
            resId = R.plurals.periodicity_every_x_weeks_at_day_of_week,
            quantity = numOfWeeks,
            args = listOf(numOfWeeks, dayOfWeek.getDisplayName(TextStyle.SHORT, locale))
        )

        override fun getPeriodicitySelectionHumanReadable(locale: Locale): TextResource {
            return TextResource.ResourceWithArgs(
                resId = R.string.periodicity_every_x_weeks_at_day_of_week_selection,
                args = listOf(numOfWeeks, dayOfWeek.getDisplayName(TextStyle.SHORT, locale))
            )
        }

        override fun compareTo(other: PeriodicityUi): Int {
            return when (other) {
                is EveryXDays -> 1
                is EveryXWeeks -> numOfWeeks.compareTo(other.numOfWeeks)
                else -> -1
            }
        }
    }

    data class EveryXMonths(
        override val nextDueDate: LocalDate? = null,
        val numOfMonth: Int,
        val dayOfMonth: Int
    ) : PeriodicityUi(nextDueDate = nextDueDate, ordinal = 2) {
        override fun getPeriodicityHumanReadable(locale: Locale) = TextResource.PluralResource(
            resId = R.plurals.periodicity_every_x_months_at_day_of_month,
            quantity = numOfMonth,
            args = listOf(numOfMonth, dayOfMonth)
        )

        override fun getPeriodicitySelectionHumanReadable(locale: Locale): TextResource {
            return TextResource.ResourceWithArgs(
                resId = R.string.periodicity_every_x_months_at_day_of_month_selection,
                args = listOf(numOfMonth, dayOfMonth)
            )
        }

        override fun compareTo(other: PeriodicityUi): Int {
            return when (other) {
                is EveryXDays, is EveryXWeeks -> 1
                is EveryXMonths -> numOfMonth.compareTo(other.numOfMonth)
                else -> -1
            }
        }
    }

    data class EveryXYears(
        override val nextDueDate: LocalDate? = null,
        val numOfYear: Int,
        val month: Month
    ) : PeriodicityUi(nextDueDate = nextDueDate, ordinal = 3) {
        override fun getPeriodicityHumanReadable(locale: Locale) = TextResource.PluralResource(
            resId = R.plurals.periodicity_every_x_years_at_month,
            quantity = numOfYear,
            args = listOf(numOfYear, month.getDisplayName(TextStyle.SHORT, locale))
        )

        override fun getPeriodicitySelectionHumanReadable(locale: Locale): TextResource {
            return TextResource.ResourceWithArgs(
                resId = R.string.periodicity_every_x_years_at_month_selection,
                args = listOf(numOfYear, month.getDisplayName(TextStyle.SHORT, locale))
            )
        }

        override fun compareTo(other: PeriodicityUi): Int {
            return when (other) {
                is EveryXDays, is EveryXWeeks, is EveryXMonths -> 1
                is EveryXYears -> numOfYear.compareTo(other.numOfYear)
            }
        }
    }
}
