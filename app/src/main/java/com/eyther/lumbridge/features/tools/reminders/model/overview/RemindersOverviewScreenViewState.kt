package com.eyther.lumbridge.features.tools.reminders.model.overview

import com.eyther.lumbridge.model.reminders.ReminderUi

sealed interface RemindersOverviewScreenViewState {
    data object Loading : RemindersOverviewScreenViewState
    data object Empty : RemindersOverviewScreenViewState

    data class Content(
        val reminders: List<ReminderUi>
    ) : RemindersOverviewScreenViewState
}
