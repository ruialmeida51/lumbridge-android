package com.eyther.lumbridge.shared.time.model

import com.eyther.lumbridge.shared.time.extensions.MONTHS_IN_YEAR
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.temporal.TemporalAdjusters

/**
 * Represents the periodicity in a given time frame.
 *
 * The idea here is to be a flexible as possible to allow for any kind of periodicity,
 * a few examples:
 *
 * - Every 2 days
 * - Every 3 weeks on a Monday
 * - Every 4 months on the 15th
 * - Every 5 years on the 3rd month
 *
 * We should be careful when using this periodicity to account for february and leap years. This class is merely
 * a representation of the periodicity and should be used in conjunction with a date to calculate whatever is needed.
 */
sealed class Periodicity(
    val ordinal: Int
) {
    companion object {
        const val EVERY_X_DAYS = 0
        const val EVERY_X_WEEKS = 1
        const val EVERY_X_MONTHS = 2
        const val EVERY_X_YEARS = 3
    }

    /**
     * It increments the start date by the number of X (given by the periodicity) until it reaches a date that is after the current date,
     * and that's why we need that `while` loop in the implementation. In theory, we could make sure to store the last date and
     * increment from there with just an if, but since this class is supposed to be completely stateless, we can't do that.
     *
     * @return The next date based on the periodicity.
     */
    abstract fun getNextDate(startFrom: LocalDate): LocalDate

    /**
     * Represents a periodicity that occurs every X days.
     *
     * @param numOfDays The number of days between each occurrence.
     */
    data class EveryXDays(
        val numOfDays: Int
    ) : Periodicity(EVERY_X_DAYS) {

        init {
            require(numOfDays > 0) { "Number of days must be greater than 0" }
        }

        override fun getNextDate(startFrom: LocalDate): LocalDate {
            val currentDate = LocalDate.now()
            var nextDate = startFrom

            while (nextDate.isBefore(currentDate)) {
                nextDate = nextDate.plusDays(numOfDays.toLong())
            }

            return nextDate
        }
    }

    /**
     * Represents a periodicity that occurs every X weeks on a given day of the week.
     *
     * @param numOfWeeks The number of weeks between each occurrence.
     * @param dayOfWeekOrdinal The day of the week the occurrence should happen.
     *
     * @see DayOfWeek
     */
    data class EveryXWeeks(
        val numOfWeeks: Int,
        val dayOfWeekOrdinal: Int
    ) : Periodicity(EVERY_X_WEEKS) {
        init {
            require(numOfWeeks > 0) { "Number of weeks must be greater than 0" }
            require(dayOfWeekOrdinal in 1..7) { "Day of week must be between 1 and 7" }
        }

        override fun getNextDate(startFrom: LocalDate): LocalDate {
            val currentDate = LocalDate.now()

            // Start with the next or same day of the week. Temporal adjusters are awesome. 🚀
            var nextDate = startFrom.with(TemporalAdjusters.nextOrSame(DayOfWeek.of(dayOfWeekOrdinal)))

            while (nextDate.isBefore(currentDate)) {
                nextDate = nextDate.plusWeeks(numOfWeeks.toLong())
            }

            return nextDate
        }
    }

    /**
     * Represents a periodicity that occurs every X months on a given day of the month.
     *
     * @param numOfMonth The number of months between each occurrence.
     * @param dayOfMonth The day of the month the occurrence should happen.
     */
    data class EveryXMonths(
        val numOfMonth: Int,
        val dayOfMonth: Int
    ) : Periodicity(EVERY_X_MONTHS) {

        init {
            require(numOfMonth > 0) { "Number of months must be greater than 0" }
            require(dayOfMonth in 1..31) { "Day of month must be between 1 and 31" }
        }

        override fun getNextDate(startFrom: LocalDate): LocalDate {
            val currentDate = LocalDate.now()

            // Start with the specified day of the month, but cap it to the max days in the starting month
            var nextDate = startFrom.withDayOfMonth(minOf(dayOfMonth, startFrom.lengthOfMonth()))

            while (nextDate.isBefore(currentDate)) {
                // When we increment a month, LocalDate internally makes sure that we don't go over the last day of the month.
                // Meaning, if we're on the 31st of January and we increment by a month, we'll end up on the 28th of February.
                // Another example is if we're on the 31th of March and we increment by a month, we'll end up on the 30th of April.
                nextDate = nextDate.plusMonths(numOfMonth.toLong())

                // If, by the reasons explained above, we're not on the same day of the month, we set it to the last day of the month.
                // This is to make sure that we're always on the same day of the month and handle edge cases like february and end
                // of month days.
                if (nextDate.dayOfMonth != dayOfMonth) {
                    nextDate = nextDate.withDayOfMonth(nextDate.lengthOfMonth())
                }
            }

            return nextDate
        }
    }

    /**
     * Represents a periodicity that occurs every X years on a given month.
     *
     * @param numOfYear The number of years between each occurrence.
     * @param monthOrdinal The month the occurrence should happen.
     */
    data class EveryXYears(
        val numOfYear: Int,
        val monthOrdinal: Int
    ) : Periodicity(EVERY_X_YEARS) {
        init {
            require(numOfYear > 0) { "Number of years must be greater than 0" }
            require(monthOrdinal in 1..12) { "Month must be between 1 and 12" }
        }

        override fun getNextDate(startFrom: LocalDate): LocalDate {
            val currentDate = LocalDate.now()
            var nextDate = startFrom.withMonth(minOf(Month.of(monthOrdinal).value, MONTHS_IN_YEAR))

            while (nextDate.isBefore(currentDate)) {
                nextDate = nextDate.plusYears(numOfYear.toLong())
            }

            return nextDate
        }
    }
}
