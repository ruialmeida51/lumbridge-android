package com.eyther.lumbridge.features.tools.tasksandreminders.model.edit

import com.eyther.lumbridge.model.time.RemindMeInUi

sealed interface RemindersEditScreenViewState {
    data object Loading : RemindersEditScreenViewState

    data class Content(
        val inputState: RemindersEditScreenInputState,
        val availableReminderTimes: List<RemindMeInUi>,
        val shouldEnableSaveButton: Boolean
    ) : RemindersEditScreenViewState
}
