package com.eyther.lumbridge.features.tools.tasksandreminders.model.edit

import com.eyther.lumbridge.ui.common.composables.model.input.DateInputState
import com.eyther.lumbridge.ui.common.composables.model.input.TextInputState

data class RemindersEditScreenInputState(
    val name: TextInputState = TextInputState(),
    val dueDate: DateInputState = DateInputState()
)
