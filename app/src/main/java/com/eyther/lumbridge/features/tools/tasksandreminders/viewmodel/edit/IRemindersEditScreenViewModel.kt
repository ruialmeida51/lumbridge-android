package com.eyther.lumbridge.features.tools.tasksandreminders.viewmodel.edit

import com.eyther.lumbridge.features.tools.tasksandreminders.model.edit.RemindersEditScreenViewEffects
import com.eyther.lumbridge.features.tools.tasksandreminders.model.edit.RemindersEditScreenViewState
import com.eyther.lumbridge.features.tools.tasksandreminders.viewmodel.edit.delegate.IRemindersEditScreenInputHandler
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IRemindersEditScreenViewModel: IRemindersEditScreenInputHandler {
    val viewState: StateFlow<RemindersEditScreenViewState>
    val viewEffects: SharedFlow<RemindersEditScreenViewEffects>

    fun saveReminder()
}
