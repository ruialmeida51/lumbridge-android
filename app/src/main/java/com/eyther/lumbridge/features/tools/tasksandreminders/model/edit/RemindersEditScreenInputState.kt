package com.eyther.lumbridge.features.tools.tasksandreminders.model.edit

import com.eyther.lumbridge.model.time.RemindMeInUi
import com.eyther.lumbridge.ui.common.composables.model.input.DateTimeInputState
import com.eyther.lumbridge.ui.common.composables.model.input.TextInputState

data class RemindersEditScreenInputState(
    val name: TextInputState = TextInputState(),
    val dueDate: DateTimeInputState = DateTimeInputState(),
    val remindMeInUi: RemindMeInUi = RemindMeInUi.FifteenMinutes()
)
