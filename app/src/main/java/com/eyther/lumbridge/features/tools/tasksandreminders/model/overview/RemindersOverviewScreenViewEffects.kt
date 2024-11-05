package com.eyther.lumbridge.features.tools.tasksandreminders.model.overview

sealed interface RemindersOverviewScreenViewEffects {
    data object CloseScreen : RemindersOverviewScreenViewEffects

    data class ShowError(
        val message: String
    ) : RemindersOverviewScreenViewEffects
}
