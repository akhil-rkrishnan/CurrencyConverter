package app.android.currency_converter.domain.repository.preferences

import app.android.currency_converter.data.local.Country
import app.android.currency_converter.data.preferences.SharedPreferenceService
import app.android.currency_converter.data.remote.currency.LatestExchangeRateResponse
import app.android.currency_converter.data.utils.asCountries
/**
* Preference repository implementation
*/
class PreferenceRepositoryImpl(
    private val preferenceService: SharedPreferenceService
): PreferenceRepository {
    override fun loadLatestExchangeRate(): LatestExchangeRateResponse? {
        return preferenceService.latestExchangeRate
    }

    override fun loadCountries(): List<Country>? {
        return preferenceService.localCurrencyMap?.asCountries()
    }

    override fun getLastSavedTimestamp(): Long? {
        return preferenceService.lastUpdatedTimeStamp
    }

    override fun getCountryName(code: String): String {
        return preferenceService.localCurrencyMap?.get(code)?: "Name not available!"
    }

    override fun saveLatestExchangeRate(latestExchangeRateResponse: LatestExchangeRateResponse) {
       preferenceService.latestExchangeRate = latestExchangeRateResponse
    }

    override fun saveCountries(map: Map<String, String>) {
        preferenceService.localCurrencyMap = map
    }

    override fun saveTimeStamp(lastSavedTimestamp: Long) {
        preferenceService.lastUpdatedTimeStamp = lastSavedTimestamp
    }

    override fun clearPreferences() {
        preferenceService.clearPreference()
    }

}