package com.eyther.lumbridge.features.tools.tasksandreminders.viewmodel.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.tools.tasksandreminders.model.edit.RemindersEditScreenInputState
import com.eyther.lumbridge.features.tools.tasksandreminders.model.edit.RemindersEditScreenViewEffects
import com.eyther.lumbridge.features.tools.tasksandreminders.model.edit.RemindersEditScreenViewState
import com.eyther.lumbridge.features.tools.tasksandreminders.viewmodel.edit.delegate.IRemindersEditScreenInputHandler
import com.eyther.lumbridge.features.tools.tasksandreminders.viewmodel.edit.delegate.RemindersEditScreenInputHandler
import com.eyther.lumbridge.model.time.RemindMeInUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemindersEditScreenViewModel @Inject constructor(
    private val remindersEditScreenInputHandler: RemindersEditScreenInputHandler
) : ViewModel(),
    IRemindersEditScreenViewModel,
    IRemindersEditScreenInputHandler by remindersEditScreenInputHandler{

    override val viewState: MutableStateFlow<RemindersEditScreenViewState> =
        MutableStateFlow(RemindersEditScreenViewState.Loading)

    override val viewEffects: MutableSharedFlow<RemindersEditScreenViewEffects> =
        MutableSharedFlow()

    init {
        viewState.update {
            RemindersEditScreenViewState.Content(
                inputState = RemindersEditScreenInputState(),
                shouldEnableSaveButton = false,
                availableReminderTimes = RemindMeInUi.getDefaults()
            )
        }

        viewModelScope.launch {
            inputState
                .onEach { inputState ->
                    viewState.update {
                        RemindersEditScreenViewState.Content(
                            inputState = inputState,
                            shouldEnableSaveButton = shouldEnableSaveButton(inputState),
                            availableReminderTimes = RemindMeInUi.getDefaults()
                        )
                    }
                }.launchIn(this)
        }
    }

    override fun saveReminder() {

    }
}
