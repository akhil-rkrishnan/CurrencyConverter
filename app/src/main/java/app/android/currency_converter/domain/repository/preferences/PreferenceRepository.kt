package app.android.currency_converter.domain.repository.preferences

import app.android.currency_converter.data.local.Country
import app.android.currency_converter.data.remote.currency.LatestExchangeRateResponse

interface PreferenceRepository {
    /**
     * Method to load latest exchange rate
     * @return latest exchange response saved from last api call
     */
    fun loadLatestExchangeRate(): LatestExchangeRateResponse?

    /**
     * Method to load the currencies
     * @return currencies saved from last api call
     */
    fun loadCountries(): List<Country>?

    /**
     * Method to get the last saved time stamp
     * @return last saved timestamp which is saved after a successful api call.
     */
    fun getLastSavedTimestamp(): Long?

    /**
     * Method to get the country name from preference
     * @return country name from list
     */
    fun getCountryName(code: String): String

    /**
     * Method to clear the preference
     */
    fun clearPreferences() {}

    /**
     * Method to save the latest exchange rate from api
     * @param latestExchangeRateResponse latest exchange response from server
     */
    fun saveLatestExchangeRate(latestExchangeRateResponse: LatestExchangeRateResponse) {}

    /**
     * Method to save countries
     * @param map variable to save the currency map from server
     */
    fun saveCountries(map: Map<String, String>) {}

    /**
     * Method to save the time stamp
     * @param lastSavedTimestamp The current time stamp in millis.
     */
    fun saveTimeStamp(lastSavedTimestamp: Long) {}

}