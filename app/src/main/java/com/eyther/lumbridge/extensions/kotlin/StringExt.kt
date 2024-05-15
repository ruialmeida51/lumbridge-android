package com.eyther.lumbridge.extensions.kotlin

/**
 * Capitalise the first letter of a string.
 * @return the capitalised string
 */
fun String.capitalise() = this.lowercase().replaceFirstChar {
    if (it.isLowerCase()) it.titlecase() else it.toString()
}
