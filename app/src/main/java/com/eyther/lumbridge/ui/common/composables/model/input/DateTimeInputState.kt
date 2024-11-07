package com.eyther.lumbridge.ui.common.composables.model.input

import com.eyther.lumbridge.ui.common.model.text.TextResource
import java.time.LocalDateTime

data class DateTimeInputState(
    val dateTime: LocalDateTime? = null,
    val error: TextResource? = null
) {
    fun isError() = error != null

    fun isValid() = !isError() && dateTime != null
}
