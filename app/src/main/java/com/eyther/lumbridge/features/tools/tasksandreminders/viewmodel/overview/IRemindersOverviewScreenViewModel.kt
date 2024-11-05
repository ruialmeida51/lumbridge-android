package com.eyther.lumbridge.features.tools.tasksandreminders.viewmodel.overview

import com.eyther.lumbridge.features.tools.tasksandreminders.model.overview.RemindersOverviewScreenViewEffects
import com.eyther.lumbridge.features.tools.tasksandreminders.model.overview.RemindersOverviewScreenViewState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IRemindersOverviewScreenViewModel {
    val viewState: StateFlow<RemindersOverviewScreenViewState>
    val viewEffects: SharedFlow<RemindersOverviewScreenViewEffects>
}
