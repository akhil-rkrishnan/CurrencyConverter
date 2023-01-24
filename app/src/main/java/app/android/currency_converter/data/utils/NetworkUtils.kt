package app.android.currency_converter.data.utils

import android.net.Network
/**
* Method to check whether internet is available
 * @return boolean
*/
fun Network.isInternetAvailable(): Boolean {
    return try {
        return getByName("www.google.com").address.isNotEmpty()
    } catch (ex: Exception) {
        false
    }
}