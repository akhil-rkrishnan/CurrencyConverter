package app.android.currency_converter.data.utils

import app.android.currency_converter.data.constants.REFRESH_TIME

/**
 * Method to check whether the time has passed
 * @return boolean
 */
fun Long?.needsRefresh(): Boolean {
    if (this == null)
        return true
    val currentTime = System.currentTimeMillis()
    val oldTime = this
    val minutes = (currentTime - oldTime) / (1000 * 60)
    return minutes >= REFRESH_TIME
}