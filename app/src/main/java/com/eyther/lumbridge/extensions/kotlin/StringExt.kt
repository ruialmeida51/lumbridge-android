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

/**
 * Replace the arguments in the route with the values provided. This is useful for
 * replacing the placeholders in the route with the actual values.
 * For example, if the route is "net_salary_overview/{$NET_SALARY_EXTRA}/{$LOCALE_EXTRA}"
 * and the values are "3000" and "PORTUGAL", the route will be replaced with the actual
 * values, such as "net_salary_overview/3000/PORTUGAL".
 *
 * @param args the arguments to replace in the route
 *        e.g mapOf("netSalary" to "1000", "locale" to "en")
 *
 * @return the route with the arguments replaced
 */
fun String.replaceArgsInRoute(args: Map<String, String>): String {
    var stringCopy = this

    args.forEach { (key, value) ->
        stringCopy = stringCopy.replace("{$key}", value)
    }

    return stringCopy
}
