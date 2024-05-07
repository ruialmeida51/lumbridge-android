package com.eyther.lumbridge.ui.common.composables.model

data class TextInputState(
    val text: String? = null,
    val error: String? = null,
    val suffix: String? = null
) {
    fun isError() = error != null
}
