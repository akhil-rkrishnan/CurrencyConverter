package app.android.currency_converter.domain.repository

import app.android.currency_converter.data.local.Country
import app.android.currency_converter.data.remote.currency.LatestExchangeRateResponse
import app.android.currency_converter.domain.repository.preferences.PreferenceRepository

/**
* Fake preference repository
*/
class FakePreferenceRepository : PreferenceRepository {
    private var shouldReturnError = false

    override fun loadLatestExchangeRate(): LatestExchangeRateResponse? {
        return if (shouldReturnError) {
            null
        } else
            LatestExchangeRateResponse("USD", "", "", rates = hashMapOf<String, Double>().apply {
                put("USD", 1.0)
                put("INR", 80.9765)
                put("JPY", 129.554)
            }, timestamp = 1674284429)
    }

    override fun loadCountries(): List<Country>? {
        return if (shouldReturnError) {
            null
        } else listOf(
            Country("USD", "United states dollar"),
            Country("INR", "Indian Ruppee"),
            Country("JPY", "Japanese Yen")
        )
    }

    override fun getLastSavedTimestamp(): Long? {
        return if (shouldReturnError) {
            null
        } else {
            0L
        }
    }

    override fun getCountryName(code: String): String {
        return if (shouldReturnError) {
            ""
        } else {
            loadCountries()?.find { it.code == code }?.name ?: "Undefined"
        }
    }

}