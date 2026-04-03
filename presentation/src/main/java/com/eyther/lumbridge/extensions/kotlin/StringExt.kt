package com.eyther.lumbridge.extensions.kotlin

import androidx.annotation.StringRes
import com.eyther.lumbridge.ui.common.model.text.TextResource

/**
 * Capitalise the first letter of a string.
 * @return the capitalised string
 */
fun String.capitalise() = this.lowercase().replaceFirstChar {
    if (it.isLowerCase()) it.titlecase() else it.toString()
}

/**
 * Helper function to get the error message of the text input.
 * This is just a boilerplate code to avoid code duplication.
 * @param errorRes the error message resource id.
 * @return the updated state with the error message of the text input.
 */
fun String?.getErrorOrNull(@StringRes errorRes: Int) = if (isNullOrEmpty()) {
    TextResource.Resource(resId = errorRes)
} else {
    null
}
