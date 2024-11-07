package com.eyther.lumbridge.features.tools.reminders.viewmodel.edit

import com.eyther.lumbridge.features.tools.reminders.model.edit.RemindersEditScreenViewEffects
import com.eyther.lumbridge.features.tools.reminders.model.edit.RemindersEditScreenViewState
import com.eyther.lumbridge.features.tools.reminders.viewmodel.edit.delegate.IRemindersEditScreenInputHandler
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IRemindersEditScreenViewModel: IRemindersEditScreenInputHandler {
    val viewState: StateFlow<RemindersEditScreenViewState>
    val viewEffects: SharedFlow<RemindersEditScreenViewEffects>

    /**
     * Called when the select time is before right now. We cannot modify the material time picker at the time of writing,
     * so we have to resort to this kind of solutions.
     */
    fun onInvalidDateTime()

    /**
     * Attempts to save the reminder currently being edited.
     */
    fun saveReminder()
}
