package com.eyther.lumbridge.features.tools.reminders.viewmodel.edit

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.R
import com.eyther.lumbridge.features.tools.navigation.ToolsNavigationItem.Reminders.Companion.ARG_REMINDER_ID
import com.eyther.lumbridge.features.tools.reminders.model.edit.RemindersEditScreenViewEffects
import com.eyther.lumbridge.features.tools.reminders.model.edit.RemindersEditScreenViewState
import com.eyther.lumbridge.features.tools.reminders.viewmodel.edit.delegate.IRemindersEditScreenInputHandler
import com.eyther.lumbridge.features.tools.reminders.viewmodel.edit.delegate.RemindersEditScreenInputHandler
import com.eyther.lumbridge.model.reminders.ReminderUi
import com.eyther.lumbridge.model.time.RemindMeInUi
import com.eyther.lumbridge.ui.common.model.text.TextResource
import com.eyther.lumbridge.usecase.reminders.GetReminderByIdUseCase
import com.eyther.lumbridge.usecase.reminders.SaveReminderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemindersEditScreenViewModel @Inject constructor(
    private val remindersEditScreenInputHandler: RemindersEditScreenInputHandler,
    private val saveReminderUseCase: SaveReminderUseCase,
    private val getReminderByIdUseCase: GetReminderByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel(),
    IRemindersEditScreenViewModel,
    IRemindersEditScreenInputHandler by remindersEditScreenInputHandler {

    companion object {
        private const val TAG = "RemindersEditScreenViewModel"
    }

    private val reminderId = requireNotNull(savedStateHandle.get<Long>(ARG_REMINDER_ID)) {
        "Reminder ID must be provided or defaulted to -1"
    }

    override val viewState: MutableStateFlow<RemindersEditScreenViewState> =
        MutableStateFlow(RemindersEditScreenViewState.Loading)

    override val viewEffects: MutableSharedFlow<RemindersEditScreenViewEffects> =
        MutableSharedFlow()

    init {
        fetchReminder()
    }

    private fun fetchReminder() {
        viewModelScope.launch {
            val reminderUi = getReminderByIdUseCase(reminderId)

            updateInput { state ->
                state.copy(
                    name = state.name.copy(
                        text = reminderUi?.label
                    ),
                    dueDate = state.dueDate.copy(
                        dateTime = reminderUi?.dueDate
                    ),
                    remindMeInputState = state.remindMeInputState.copy(
                        remindMeInUi = reminderUi?.remindMeIn ?: RemindMeInUi.defaultFromOrdinal(0)
                    ),
                    nDaysBeforeInput = state.nDaysBeforeInput.copy(
                        text = (reminderUi?.remindMeIn as? RemindMeInUi.XDaysBefore)?.days?.toString()
                    ),
                    nHoursBeforeInput = state.nHoursBeforeInput.copy(
                        text = (reminderUi?.remindMeIn as? RemindMeInUi.XHoursBefore)?.hours?.toString()
                    ),
                    nMinutesBeforeInput = state.nMinutesBeforeInput.copy(
                        text = (reminderUi?.remindMeIn as? RemindMeInUi.XMinutesBefore)?.minutes?.toString()
                    )
                )
            }

            inputState
                .onEach { inputState ->
                    viewState.update {
                        RemindersEditScreenViewState.Content(
                            inputState = inputState,
                            availableReminderTimes = RemindMeInUi.getDefaults(),
                            shouldEnableSaveButton = shouldEnableSaveButton(inputState)
                        )
                    }
                }.launchIn(this)
        }
    }

    override fun onInvalidDateTime() {
        viewModelScope.launch {
            viewEffects.emit(
                RemindersEditScreenViewEffects.ShowError(
                    errorMessage = TextResource.Resource(R.string.tools_reminders_invalid_date)
                )
            )
        }
    }

    override fun saveReminder() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "💥 Error saving reminder", throwable)
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            val inputState = inputState.value

            val reminderUi = ReminderUi(
                reminderId = reminderId,
                label = checkNotNull(inputState.name.text),
                dueDate = checkNotNull(inputState.dueDate.dateTime),
                remindMeIn = when (val remindMeInUi = inputState.remindMeInputState.remindMeInUi) {
                    is RemindMeInUi.XDaysBefore -> RemindMeInUi.XDaysBefore(
                        days = checkNotNull(inputState.nDaysBeforeInput.text?.toIntOrNull())
                    )

                    is RemindMeInUi.XHoursBefore -> RemindMeInUi.XHoursBefore(
                        hours = checkNotNull(inputState.nHoursBeforeInput.text?.toIntOrNull())
                    )

                    is RemindMeInUi.XMinutesBefore -> RemindMeInUi.XMinutesBefore(
                        minutes = checkNotNull(inputState.nMinutesBeforeInput.text?.toIntOrNull())
                    )

                    else -> remindMeInUi
                },
                alreadyNotified = false
            )

            saveReminderUseCase(reminderUi)
            viewEffects.emit(RemindersEditScreenViewEffects.CloseScreen)
        }
    }
}
