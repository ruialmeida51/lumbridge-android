package com.eyther.lumbridge.features.tools.reminders.model.edit

import com.eyther.lumbridge.ui.common.composables.model.input.DateTimeInputState
import com.eyther.lumbridge.ui.common.composables.model.input.TextInputState

data class RemindersEditScreenInputState(
    val name: TextInputState = TextInputState(),
    val dueDate: DateTimeInputState = DateTimeInputState(),
    val remindMeInputState: RemindMeInInputState = RemindMeInInputState(),
    val nDaysBeforeInput: TextInputState = TextInputState(),
    val nHoursBeforeInput: TextInputState = TextInputState(),
    val nMinutesBeforeInput: TextInputState = TextInputState()
)
