package app.android.currency_converter.data.utils

import com.google.gson.Gson

/**
* Method to parse Class type T from json string
 * @param destination class type for type T (generic)
 * @return class T
*/
fun <T : Any> String?.fromJson(destination: Class<T>): T {
    return Gson().fromJson(this, destination)
}

/**
 * Method to convert class T type object to json
 * @return corresponding string object for class type T
 */
fun <T> T.toJsonString(): String {
    return Gson().toJson(this)
}
