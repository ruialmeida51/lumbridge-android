package com.eyther.lumbridge.extensions.kotlin

fun String.capitalise() = this.lowercase().replaceFirstChar {
    if (it.isLowerCase()) it.titlecase() else it.toString()
}
