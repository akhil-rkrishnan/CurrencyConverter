package app.android.currency_converter.data.preferences

import android.content.SharedPreferences
import app.android.currency_converter.data.constants.KEY_CURRENCY_MAP
import app.android.currency_converter.data.constants.KEY_LAST_SAVED_TIMESTAMP
import app.android.currency_converter.data.constants.KEY_LATEST_EXCHANGE_RATE
import app.android.currency_converter.data.remote.currency.LatestExchangeRateResponse
import app.android.currency_converter.data.utils.fromJson
import app.android.currency_converter.data.utils.toJsonString

/**
* This class is used for saving data to shared preference.
 * @property preferences SharedPreference object
*/
class SharedPreferenceService(private val preferences: SharedPreferences) {
    private val editor = preferences.edit()

    // variable to store currency list from api
    var localCurrencyMap: Map<String, String>?
        get() = try {
            val savedCurrencyMap = preferences.getString(KEY_CURRENCY_MAP, null)
            if (!savedCurrencyMap.isNullOrEmpty()) {
                savedCurrencyMap.fromJson(Map::class.java) as Map<String, String>?
            } else {
                null
            }
        } catch (ex: Exception) {
            null
        }
        set(value) = editor.putString(KEY_CURRENCY_MAP, value?.toJsonString()).apply()

    // variable to save the latest exchange rate from api
    var latestExchangeRate: LatestExchangeRateResponse?
        get() = try {
            val savedExchangeRate = preferences.getString(KEY_LATEST_EXCHANGE_RATE, null)
            if (!savedExchangeRate.isNullOrEmpty()) {
                savedExchangeRate.fromJson(LatestExchangeRateResponse::class.java)
            } else {
                null
            }
        } catch (ex: Exception) {
            null
        }
        set(value) = editor.putString(KEY_LATEST_EXCHANGE_RATE, value?.toJsonString()).apply()

    // variable to store last updated timestamp
    var lastUpdatedTimeStamp: Long?
        get() = try {
            val lastValue = preferences.getLong(KEY_LAST_SAVED_TIMESTAMP, 0L)
            if (lastValue == 0L) null else lastValue
        } catch (ex: Exception) {
            null
        }
        set(value) = editor.putLong(KEY_LAST_SAVED_TIMESTAMP, value ?: 0L).apply()

    /**
    * Method to clear preference
    */
    fun clearPreference() {
        editor.clear().apply()
    }
}