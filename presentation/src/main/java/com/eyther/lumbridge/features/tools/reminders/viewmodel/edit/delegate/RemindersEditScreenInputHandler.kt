package com.eyther.lumbridge.features.tools.reminders.viewmodel.edit.delegate

import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.getErrorOrNull
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewState.Content
import com.eyther.lumbridge.features.tools.reminders.model.edit.RemindMeInInputState
import com.eyther.lumbridge.features.tools.reminders.model.edit.RemindersEditScreenInputState
import com.eyther.lumbridge.model.time.RemindMeInUi
import com.eyther.lumbridge.shared.time.extensions.toLocalDateTime
import com.eyther.lumbridge.ui.common.model.text.TextResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime
import javax.inject.Inject

class RemindersEditScreenInputHandler @Inject constructor() : IRemindersEditScreenInputHandler {

    override val inputState = MutableStateFlow(RemindersEditScreenInputState())

    override fun onNameChanged(name: String?) {
        updateInput { state ->
            state.copy(
                name = state.name.copy(
                    text = name,
                    error = name.getErrorOrNull(
                        R.string.recurring_payment_edit_invalid_name
                    )
                )
            )
        }
    }

    override fun onDueDateChanged(dueDate: Long?) {
        updateInput { state ->
            state.copy(
                dueDate = state.dueDate.copy(
                    dateTime = dueDate?.toLocalDateTime(),
                    error = dueDate?.toString().getErrorOrNull(
                        R.string.invalid_date
                    )
                ),
                remindMeInputState = getNewRemindMeInputState(
                    remindMeInOrdinal = state.remindMeInputState.remindMeInUi.ordinal,
                    currentState = state,
                    dueDate = dueDate?.toLocalDateTime()
                )
            )
        }
    }

    override fun onRemindMeInTypeChanged(remindMeInOrdinal: Int?) {
        updateInput { state ->
            state.copy(
                remindMeInputState = getNewRemindMeInputState(
                    remindMeInOrdinal = remindMeInOrdinal,
                    currentState = state
                )
            )
        }
    }

    override fun onReminderDaysInputChanged(days: Int?) {
        updateInput { state ->
            val daysText = if (validateReminderDays(days ?: 0)) {
                days?.toString()
            } else {
                null
            }

            state.copy(
                nDaysBeforeInput = state.nDaysBeforeInput.copy(
                    text = daysText,
                    error = daysText.getErrorOrNull(
                        R.string.tools_reminder_n_input_invalid
                    )
                )
            )
        }
    }

    override fun onReminderHoursInputChanged(hours: Int?) {
        updateInput { state ->
            val hoursText = if (validateReminderHours(hours ?: 0)) {
                hours?.toString()
            } else {
                null
            }

            state.copy(
                nHoursBeforeInput = state.nHoursBeforeInput.copy(
                    text = hoursText,
                    error = hoursText.getErrorOrNull(
                        R.string.tools_reminder_n_input_invalid
                    )
                )
            )
        }
    }

    override fun onReminderMinutesInputChanged(minutes: Int?) {
        updateInput { state ->
            val minutesText = if (validateReminderMinutes(minutes ?: 0)) {
                minutes?.toString()
            } else {
                null
            }

            state.copy(
                nMinutesBeforeInput = state.nMinutesBeforeInput.copy(
                    text = minutesText,
                    error = minutesText.getErrorOrNull(
                        R.string.tools_reminder_n_input_invalid
                    )
                )
            )
        }
    }

    /**
     * Checks if the save button should be enabled.
     *
     * The button should be enabled if the user has entered valid data.
     *
     * @param inputState the current state of the screen.
     * @return true if the button should be enabled, false otherwise.
     */
    override fun shouldEnableSaveButton(inputState: RemindersEditScreenInputState): Boolean {
        return inputState.name.isValid() &&
            inputState.dueDate.isValid() &&
            validateRemindMeIn(inputState.remindMeInputState.remindMeInUi)
    }

    /**
     * Helper function to update the inputState state of the screen.
     *
     * @param update the function to update the content state.
     * @see Content
     */
    override fun updateInput(
        update: (RemindersEditScreenInputState) -> RemindersEditScreenInputState
    ) {
        inputState.update { currentState ->
            update(currentState)
        }
    }

    /**
     * Helper function to be reusable. THis is needed because when we change the date, we need to validate the remind me in
     * time again, to check if the reminder time is after the current time.
     *
     * @param remindMeInOrdinal the ordinal of the remind me in time.
     * @param currentState the current state of the screen.
     */
    private fun getNewRemindMeInputState(
        remindMeInOrdinal: Int?,
        dueDate: LocalDateTime? = inputState.value.dueDate.dateTime,
        currentState: RemindersEditScreenInputState
    ): RemindMeInInputState {
        val newRemindMeInUi = RemindMeInUi.defaultFromOrdinal(remindMeInOrdinal ?: 0)

        return currentState.remindMeInputState.copy(
            remindMeInUi = newRemindMeInUi,
            errorText = if (newRemindMeInUi.isCustom() || validateRemindMeIn(newRemindMeInUi, dueDate)) {
                null
            } else {
                TextResource.Resource(R.string.tools_reminder_n_input_invalid)
            }
        )
    }

    /**
     * Validates the remind me in time. Given different options for reminder time, we need to validate each one of them
     * with the proper logic to ensure the user has selected a valid option, as they differ from a predetermined set of
     * times to choose from to a dynamic input.
     */
    private fun validateRemindMeIn(
        remindMeInUi: RemindMeInUi,
        dueDate: LocalDateTime? = inputState.value.dueDate.dateTime
    ): Boolean {
        return when (remindMeInUi) {
            is RemindMeInUi.XDaysBefore -> validateReminderDays(remindMeInUi.days, dueDate)
            is RemindMeInUi.XHoursBefore -> validateReminderHours(remindMeInUi.hours, dueDate)
            is RemindMeInUi.XMinutesBefore -> validateReminderMinutes(remindMeInUi.minutes, dueDate)
            is RemindMeInUi.FifteenMinutes -> validateIfReminderDateIsAfterRightNow(dueDate) { it.minusMinutes(15) }
            is RemindMeInUi.ThirtyMinutes -> validateIfReminderDateIsAfterRightNow(dueDate) { it.minusMinutes(30) }
            is RemindMeInUi.OneHour -> validateIfReminderDateIsAfterRightNow(dueDate) { it.minusHours(1) }
            is RemindMeInUi.TwoHours -> validateIfReminderDateIsAfterRightNow(dueDate) { it.minusHours(2) }
            is RemindMeInUi.OneDay -> validateIfReminderDateIsAfterRightNow(dueDate) { it.minusDays(1) }
            is RemindMeInUi.TwoDays -> validateIfReminderDateIsAfterRightNow(dueDate) { it.minusDays(2) }
        }
    }

    private fun validateReminderDays(
        days: Int?,
        dueDate: LocalDateTime? = inputState.value.dueDate.dateTime
    ): Boolean {
        return days != null && days > 0 && validateIfReminderDateIsAfterRightNow(dueDate) { it.minusDays(days.toLong()) }
    }

    private fun validateReminderHours(
        hours: Int?,
        dueDate: LocalDateTime? = inputState.value.dueDate.dateTime,
    ): Boolean {
        return hours != null && hours > 0 && validateIfReminderDateIsAfterRightNow(dueDate) { it.minusHours(hours.toLong()) }
    }

    private fun validateReminderMinutes(
        minutes: Int?,
        dueDate: LocalDateTime? = inputState.value.dueDate.dateTime,
    ): Boolean {
        return minutes != null && minutes > 0 && validateIfReminderDateIsAfterRightNow(dueDate) { it.minusMinutes(minutes.toLong()) }
    }

    private fun validateIfReminderDateIsAfterRightNow(
        dueDate: LocalDateTime?,
        reminderTimeCallback: (LocalDateTime) -> LocalDateTime
    ): Boolean {
        if (dueDate == null) return false

        val now = LocalDateTime.now()
        val reminderTime = reminderTimeCallback(dueDate)

        return reminderTime.isAfter(now)
    }
}
