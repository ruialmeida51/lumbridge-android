package com.eyther.lumbridge.features.tools.tasksandreminders.model.edit

interface RemindersEditScreenViewEffects {
    data object CloseScreen : RemindersEditScreenViewEffects

    data class ShowError(
        val message: String
    ) : RemindersEditScreenViewEffects
}
