package com.eyther.lumbridge.ui.common.composables.model

import com.eyther.lumbridge.ui.common.model.text.TextResource

data class TextInputState(
    val text: String? = null,
    val error: TextResource? = null,
    val suffix: String? = null
) {
    fun isError() = error != null

    fun isValid() = !isError() && text != null
}
