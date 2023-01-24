package app.android.currency_converter.data.utils

import java.util.*

fun String.assignDefaultIfNotValid(fallback: String = "1") : String {
    if (this.isValidNumber())
        return this
    if (fallback.isValidNumber())
        return fallback
    return "1"
}

fun String?.isEmptyOrBlank(): Boolean {
    if (this == null) {
        return true
    }
    val trimmed = this.trim()
    return trimmed.isEmpty()
}

fun String?.isNotEmptyOrBlank(): Boolean {
    return !isEmptyOrBlank()
}

fun String.isValidNumber(): Boolean {
    if (this.isEmptyOrBlank()) {
        return false
    }
    val toDouble = this.toDoubleOrNull()
    return toDouble != null && toDouble > 0
}

fun Double.formattedDouble(decimalPlaces: Int = 3): String {
    if (this == 0.0) {
        return "0"
    }
    return Formatter().format("%.${decimalPlaces}f", this).toString()
}


