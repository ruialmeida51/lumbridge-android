package com.eyther.lumbridge.features.tools.reminders.viewmodel.edit.delegate

import com.eyther.lumbridge.features.tools.reminders.model.edit.RemindersEditScreenInputState
import kotlinx.coroutines.flow.StateFlow

interface IRemindersEditScreenInputHandler {
    val inputState: StateFlow<RemindersEditScreenInputState>

    /**
     * Methods called on different input changes. Each one updates the input state with
     * the new value and performs any necessary validation.
     */
    fun onNameChanged(name: String?)
    fun onDueDateChanged(dueDate: Long?)
    fun onRemindMeInTypeChanged(remindMeInOrdinal: Int?)
    fun onReminderDaysInputChanged(days: Int?)
    fun onReminderHoursInputChanged(hours: Int?)
    fun onReminderMinutesInputChanged(minutes: Int?)

    /**
     * Checks if we have enough information available to enable the button.
     * @return true if we have enough information to enable the button, false otherwise.
     * @see RemindersEditScreenInputState
     */
    fun shouldEnableSaveButton(inputState: RemindersEditScreenInputState): Boolean

    /**
     * Helper function to update the input state.
     * @param update the lambda to update the input state.
     * @see RemindersEditScreenInputState
     */
    fun updateInput(update: (RemindersEditScreenInputState) -> RemindersEditScreenInputState)
}
