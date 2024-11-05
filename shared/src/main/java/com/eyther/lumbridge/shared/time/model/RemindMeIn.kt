package com.eyther.lumbridge.shared.time.model

import java.time.LocalDateTime

/**
 * Represents a time period to remind the user in. We provide a few predefined options, but also allow for custom times.
 */
sealed class RemindMeIn(val ordinal: Int) {

    /**
     * Returns the time to remind the user in, starting from the given time.
     *
     * @param dueTime The time when a reminder is due. This is the time we will start from to calculate when to notify the user.
     *
     * @return The time to remind the user in.
     */
    abstract fun getReminderTime(dueTime: LocalDateTime): LocalDateTime

    data object FifteenMinutes: RemindMeIn(ordinal = 0) {
        override fun getReminderTime(dueTime: LocalDateTime): LocalDateTime {
            return dueTime.minusMinutes(15)
        }
    }

    data object ThirtyMinutes: RemindMeIn(ordinal = 1) {
        override fun getReminderTime(dueTime: LocalDateTime): LocalDateTime {
            return dueTime.minusMinutes(30)
        }
    }

    data object OneHour: RemindMeIn(ordinal = 2) {
        override fun getReminderTime(dueTime: LocalDateTime): LocalDateTime {
            return dueTime.minusHours(1)
        }
    }

    data object TwoHours: RemindMeIn(ordinal = 3) {
        override fun getReminderTime(dueTime: LocalDateTime): LocalDateTime {
            return dueTime.minusHours(2)
        }
    }

    data object OneDay: RemindMeIn(ordinal = 4) {
        override fun getReminderTime(dueTime: LocalDateTime): LocalDateTime {
            return dueTime.minusDays(1)
        }
    }

    data object TwoDays: RemindMeIn(ordinal = 5) {
        override fun getReminderTime(dueTime: LocalDateTime): LocalDateTime {
            return dueTime.minusDays(2)
        }
    }

    data class XMinutesBefore(val minutes: Int): RemindMeIn(ordinal = 6) {
        init {
            require(minutes > 0) { "Number of minutes must be greater than 0" }
        }

        override fun getReminderTime(dueTime: LocalDateTime): LocalDateTime {
            return dueTime.minusMinutes(minutes.toLong())
        }
    }

    data class XHoursBefore(val hours: Int): RemindMeIn(ordinal = 7) {
        init {
            require(hours > 0) { "Number of hours must be greater than 0" }
        }

        override fun getReminderTime(dueTime: LocalDateTime): LocalDateTime {
            return dueTime.minusHours(hours.toLong())
        }
    }

    data class XDaysBefore(val days: Int): RemindMeIn(ordinal = 8) {
        init {
            require(days > 0) { "Number of days must be greater than 0" }
        }

        override fun getReminderTime(dueTime: LocalDateTime): LocalDateTime {
            return dueTime.minusDays(days.toLong())
        }
    }
}
