package com.eyther.lumbridge.features.tools.tasksandreminders.viewmodel.edit.delegate

import com.eyther.lumbridge.features.tools.tasksandreminders.model.edit.RemindersEditScreenInputState
import com.eyther.lumbridge.ui.common.model.text.TextResource
import kotlinx.coroutines.flow.StateFlow

interface IRemindersEditScreenInputHandler {
    val inputState: StateFlow<RemindersEditScreenInputState>

    /**
     * Methods called on different input changes. Each one updates the input state with
     * the new value and performs any necessary validation.
     */
    fun onNameChanged(name: String?)
    fun onDueDateChanged(dueDate: Long?)

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
