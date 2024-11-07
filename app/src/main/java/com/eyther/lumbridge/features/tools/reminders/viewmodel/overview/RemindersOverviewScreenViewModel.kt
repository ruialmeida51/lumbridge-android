package com.eyther.lumbridge.features.tools.reminders.viewmodel.overview

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.tools.reminders.model.overview.RemindersOverviewScreenViewEffects
import com.eyther.lumbridge.features.tools.reminders.model.overview.RemindersOverviewScreenViewState
import com.eyther.lumbridge.usecase.reminders.DeleteReminderUseCase
import com.eyther.lumbridge.usecase.reminders.GetRemindersFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemindersOverviewScreenViewModel @Inject constructor(
    private val getRemindersFlowUseCase: GetRemindersFlowUseCase,
    private val deleteReminderUseCase: DeleteReminderUseCase
) : ViewModel(),
    IRemindersOverviewScreenViewModel {

    companion object {
        private const val TAG = "RemindersOverviewScreenViewModel"
    }

    override val viewState: MutableStateFlow<RemindersOverviewScreenViewState> =
        MutableStateFlow(RemindersOverviewScreenViewState.Loading)

    override val viewEffects: MutableSharedFlow<RemindersOverviewScreenViewEffects> =
        MutableSharedFlow()

    init {
        fetchReminders()
    }

    private fun fetchReminders() {
        viewModelScope.launch {
            getRemindersFlowUseCase()
                .onEach { reminders ->
                    if (reminders.isEmpty()) {
                        viewState.update { RemindersOverviewScreenViewState.Empty }
                        return@onEach
                    }

                    viewState.update {
                        RemindersOverviewScreenViewState.Content(reminders)
                    }
                }
                .catch {
                    Log.e(TAG, "💥 Failed to fetch reminders", it)
                    viewState.update {
                        RemindersOverviewScreenViewState.Empty
                    }
                }
                .launchIn(this)
        }
    }

    override fun deleteReminder(reminderId: Long) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "💥 Failed to delete reminder", throwable)
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            deleteReminderUseCase(reminderId)
        }
    }
}
