package com.eyther.lumbridge.features.tools.tasksandreminders.viewmodel.edit.delegate

import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.getErrorOrNull
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewState.Content
import com.eyther.lumbridge.features.tools.tasksandreminders.model.edit.RemindersEditScreenInputState
import com.eyther.lumbridge.model.time.RemindMeInUi
import com.eyther.lumbridge.shared.time.extensions.toLocalDateTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
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
                )
            )
        }
    }

    override fun onRemindMeInTypeChanged(remindMeInOrdinal: Int?) {
        updateInput { state ->
            state.copy(
                remindMeInUi = RemindMeInUi.defaultFromOrdinal(remindMeInOrdinal ?: 0)
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
            validateRemindMeIn(inputState.remindMeInUi)
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
     * Validates the remind me in time. Given different options for reminder time, we need to validate each one of them
     * with the proper logic to ensure the user has selected a valid option, as they differ from a predetermined set of
     * times to choose from to a dynamic input.
     */
    private fun validateRemindMeIn(remindMeInUi: RemindMeInUi): Boolean {
        return when (remindMeInUi) {
            is RemindMeInUi.XDaysBefore -> validateReminderMeInDays(remindMeInUi.days)
            is RemindMeInUi.XHoursBefore -> validateReminderMeInHours(remindMeInUi.hours)
            is RemindMeInUi.XMinutesBefore -> validateReminderMeInMinutes(remindMeInUi.minutes)
            else -> true
        }
    }

    private fun validateReminderMeInDays(days: Int?): Boolean {
        return days != null && days > 0
    }

    private fun validateReminderMeInHours(hours: Int?): Boolean {
        return hours != null && hours > 0
    }

    private fun validateReminderMeInMinutes(minutes: Int?): Boolean {
        return minutes != null && minutes > 0
    }
}
