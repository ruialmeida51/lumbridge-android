package com.eyther.lumbridge.model.time

import com.eyther.lumbridge.R
import com.eyther.lumbridge.ui.common.model.text.TextResource
import java.time.LocalDate

sealed class RemindMeInUi(
    open val reminderTime: LocalDate? = null,
    open val ordinal: Int
) : Comparable<RemindMeInUi> {

    companion object {
        fun getDefaults() = listOf(
            FifteenMinutes(),
            ThirtyMinutes(),
            OneHour(),
            TwoHours(),
            OneDay(),
            TwoDays(),
            XMinutesBefore(5),
            XHoursBefore(1),
            XDaysBefore(1)
        )

        fun defaultFromOrdinal(ordinal: Int) = getDefaults().first { it.ordinal == ordinal }
    }

    abstract fun getPeriodicityHumanReadable(): TextResource
    abstract fun getPeriodicitySelectionHumanReadable(): TextResource

    // Single compareTo for ordinal-based ordering
    override fun compareTo(other: RemindMeInUi): Int {
        return this.ordinal.compareTo(other.ordinal)
    }

    data class FifteenMinutes(
        override val reminderTime: LocalDate? = null,
    ) : RemindMeInUi(reminderTime = reminderTime, ordinal = 0) {
        override fun getPeriodicityHumanReadable() =
            TextResource.Resource(R.string.tools_reminder_fifteen_minutes_display)

        override fun getPeriodicitySelectionHumanReadable() =
            TextResource.Resource(R.string.tools_reminders_fifteen_minutes_before_selection)
    }

    data class ThirtyMinutes(
        override val reminderTime: LocalDate? = null
    ) : RemindMeInUi(reminderTime = reminderTime, ordinal = 1) {
        override fun getPeriodicityHumanReadable() =
            TextResource.Resource(R.string.tools_reminder_thirty_minutes_display)

        override fun getPeriodicitySelectionHumanReadable() =
            TextResource.Resource(R.string.tools_reminders_thirty_minutes_before_selection)
    }

    data class OneHour(
        override val reminderTime: LocalDate? = null,
    ) : RemindMeInUi(reminderTime = reminderTime, ordinal = 2) {
        override fun getPeriodicityHumanReadable() =
            TextResource.Resource(R.string.tools_reminder_one_hour_display)

        override fun getPeriodicitySelectionHumanReadable() =
            TextResource.Resource(R.string.tools_reminders_one_hour_before_selection)
    }

    data class TwoHours(
        override val reminderTime: LocalDate? = null,
    ) : RemindMeInUi(reminderTime = reminderTime, ordinal = 3) {
        override fun getPeriodicityHumanReadable() =
            TextResource.Resource(R.string.tools_reminder_two_hours_display)

        override fun getPeriodicitySelectionHumanReadable() =
            TextResource.Resource(R.string.tools_reminders_two_hours_before_selection)
    }

    data class OneDay(
        override val reminderTime: LocalDate? = null,
    ) : RemindMeInUi(reminderTime = reminderTime, ordinal = 4) {
        override fun getPeriodicityHumanReadable() =
            TextResource.Resource(R.string.tools_reminder_one_day_display)

        override fun getPeriodicitySelectionHumanReadable() =
            TextResource.Resource(R.string.tools_reminders_one_day_before_selection)
    }

    data class TwoDays(
        override val reminderTime: LocalDate? = null,
    ) : RemindMeInUi(reminderTime = reminderTime, ordinal = 5) {
        override fun getPeriodicityHumanReadable() =
            TextResource.Resource(R.string.tools_reminder_two_days_display)

        override fun getPeriodicitySelectionHumanReadable() =
            TextResource.Resource(R.string.tools_reminders_two_days_before_selection)
    }

    data class XMinutesBefore(val minutes: Int) : RemindMeInUi(ordinal = 6) {
        override fun getPeriodicityHumanReadable() =
            TextResource.PluralResource(R.plurals.tools_reminder_n_minutes_before_display, minutes, listOf(minutes))

        override fun getPeriodicitySelectionHumanReadable() =
            TextResource.Resource(R.string.tools_reminder_n_minutes_before_selection)

        override fun compareTo(other: RemindMeInUi): Int {
            return when (other) {
                is XMinutesBefore -> minutes.compareTo(other.minutes)
                else -> super.compareTo(other)
            }
        }
    }

    data class XHoursBefore(val hours: Int) : RemindMeInUi(ordinal = 7) {
        override fun getPeriodicityHumanReadable() =
            TextResource.PluralResource(R.plurals.tools_reminder_n_hours_before_display, hours, listOf(hours))

        override fun getPeriodicitySelectionHumanReadable() =
            TextResource.Resource(R.string.tools_reminder_n_hours_before_selection)

        override fun compareTo(other: RemindMeInUi): Int {
            return when (other) {
                is XHoursBefore -> hours.compareTo(other.hours)
                else -> super.compareTo(other)
            }
        }
    }

    data class XDaysBefore(val days: Int) : RemindMeInUi(ordinal = 8) {
        override fun getPeriodicityHumanReadable() =
            TextResource.PluralResource(R.plurals.tools_reminder_n_days_before_display, days, listOf(days))

        override fun getPeriodicitySelectionHumanReadable() =
            TextResource.Resource(R.string.tools_reminder_n_days_before_selection)

        override fun compareTo(other: RemindMeInUi): Int {
            return when (other) {
                is XDaysBefore -> days.compareTo(other.days)
                else -> super.compareTo(other)
            }
        }
    }
}
