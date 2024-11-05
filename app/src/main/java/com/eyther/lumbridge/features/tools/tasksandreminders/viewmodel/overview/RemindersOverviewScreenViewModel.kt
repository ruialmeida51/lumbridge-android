package com.eyther.lumbridge.features.tools.tasksandreminders.viewmodel.overview

import androidx.lifecycle.ViewModel
import com.eyther.lumbridge.features.tools.tasksandreminders.model.overview.RemindersOverviewScreenViewEffects
import com.eyther.lumbridge.features.tools.tasksandreminders.model.overview.RemindersOverviewScreenViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RemindersOverviewScreenViewModel @Inject constructor() : ViewModel(), IRemindersOverviewScreenViewModel {

    override val viewState: MutableStateFlow<RemindersOverviewScreenViewState> =
        MutableStateFlow(RemindersOverviewScreenViewState.Loading)

    override val viewEffects: MutableSharedFlow<RemindersOverviewScreenViewEffects> =
        MutableSharedFlow()

    init {
        viewState.update {
            RemindersOverviewScreenViewState.Empty
        }
    }
}
