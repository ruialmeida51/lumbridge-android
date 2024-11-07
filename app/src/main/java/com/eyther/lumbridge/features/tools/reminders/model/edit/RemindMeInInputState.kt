package com.eyther.lumbridge.features.tools.reminders.model.edit

import com.eyther.lumbridge.model.time.RemindMeInUi
import com.eyther.lumbridge.ui.common.model.text.TextResource

data class RemindMeInInputState(
    val remindMeInUi: RemindMeInUi = RemindMeInUi.FifteenMinutes(),
    val errorText: TextResource? = null
) {
    fun isValid() = errorText == null
    fun isError() = errorText != null
}
