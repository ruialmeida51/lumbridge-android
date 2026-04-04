package com.eyther.lumbridge.features.tools.reminders.viewmodel.overview

import com.eyther.lumbridge.features.tools.reminders.model.overview.RemindersOverviewScreenViewEffects
import com.eyther.lumbridge.features.tools.reminders.model.overview.RemindersOverviewScreenViewState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IRemindersOverviewScreenViewModel {
    val viewState: StateFlow<RemindersOverviewScreenViewState>
    val viewEffects: SharedFlow<RemindersOverviewScreenViewEffects>

    fun deleteReminder(reminderId: Long)
}
