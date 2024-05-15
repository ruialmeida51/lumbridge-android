package com.eyther.lumbridge.ui.common.composables.model

import com.eyther.lumbridge.ui.common.model.text.TextResource
import java.time.LocalDate

data class DateInputState(
    val date: LocalDate? = null,
    val error: TextResource? = null
) {
    fun isError() = error != null
}
