package com.eyther.lumbridge.features.tools.tasksandreminders.model.overview

sealed interface RemindersOverviewScreenViewState {
    data object Loading : RemindersOverviewScreenViewState
    data object Empty : RemindersOverviewScreenViewState

    data class Content(
        val reminders: List<*>
    ) : RemindersOverviewScreenViewState
}
