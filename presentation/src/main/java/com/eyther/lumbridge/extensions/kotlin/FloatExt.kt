package com.eyther.lumbridge.extensions.kotlin

import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols

/**
 * Format a float to two decimal places. This will force two decimal places even if the float is a whole number.
 * @return the formatted float
 */
fun Float.forceTwoDecimalsPlaces(): String {
    val decimalFormatSymbols = DecimalFormatSymbols()
    decimalFormatSymbols.decimalSeparator = '.'

    val decimalFormatter = DecimalFormat("##.##", decimalFormatSymbols)
    decimalFormatter.maximumFractionDigits = 2
    decimalFormatter.minimumFractionDigits = 2

    return decimalFormatter.format(this)
}

/**
 * Format a float to two decimal places. This will not show the decimal places if the float is a whole number.
 * @return the formatted float
 */
fun Float.twoDecimalPlaces(): String {
    val decimalFormatSymbols = DecimalFormatSymbols()
    decimalFormatSymbols.decimalSeparator = '.'

    val decimalFormatter = DecimalFormat("##.##", decimalFormatSymbols)
    decimalFormatter.maximumFractionDigits = 2

    return decimalFormatter.format(this)
}
