package com.eyther.lumbridge.extensions.kotlin

import android.icu.text.DecimalFormat

/**
 * Format a float to two decimal places.
 * @return the formatted float
 */
fun Float.twoDecimalPlaces(): String {
    val decimalFormatter = DecimalFormat("#.##")
    decimalFormatter.maximumFractionDigits = 2
    decimalFormatter.minimumFractionDigits = 2
    return decimalFormatter.format(this)
}
