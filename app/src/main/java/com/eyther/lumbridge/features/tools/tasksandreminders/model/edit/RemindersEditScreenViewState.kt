package com.eyther.lumbridge.features.tools.tasksandreminders.model.edit

sealed interface RemindersEditScreenViewState {
    data object Loading : RemindersEditScreenViewState

    data class Content(
        val inputState: RemindersEditScreenInputState,
        val shouldEnableSaveButton: Boolean
    ) : RemindersEditScreenViewState
}
