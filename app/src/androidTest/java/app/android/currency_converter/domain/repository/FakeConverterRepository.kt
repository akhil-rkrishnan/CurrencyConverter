package app.android.currency_converter.domain.repository

import app.android.currency_converter.data.remote.currency.LatestExchangeRateResponse
import app.android.currency_converter.data.utils.ApiResult
import app.android.currency_converter.domain.repository.converter.ConverterRepository
import app.android.currency_converter.domain.repository.localConversion.LocalConversionRepository
import app.android.currency_converter.domain.repository.preferences.PreferenceRepository
import app.android.currency_converter.presentation.utils.UiText
import com.google.common.annotations.VisibleForTesting

/**
* Fake converter repository class
*/
class FakeConverterRepository(
    private val preferenceRepository: PreferenceRepository,
    private val localConversionRepository: LocalConversionRepository
) : ConverterRepository {

    private var shouldReturnError: Boolean = false

    override suspend fun getLatestExchangeRate(
        base_country: String,
        pretty_print: Boolean,
        show_alternative: Boolean
    ): ApiResult<LatestExchangeRateResponse> {
        return if (shouldReturnError) {
            ApiResult.Failed(UiText.DynamicString("Api Error"))
        } else {
            val latestExchangeRateResponse = LatestExchangeRateResponse(
                "USD",
                "",
                "",
                rates = hashMapOf<String, Double>().apply {
                    put("USD", 1.0)
                    put("INR", 80.9765)
                    put("JPY", 129.554)
                },
                timestamp = 1674284429
            )
            preferenceRepository.saveLatestExchangeRate(latestExchangeRateResponse)
            ApiResult.Success(latestExchangeRateResponse)
        }
    }

    override suspend fun getCurrencies(
        pretty_print: Boolean,
        show_alternative: Boolean,
        show_inactive: Boolean
    ): ApiResult<Map<String, String>> {
        return if (shouldReturnError) {
            ApiResult.Failed(UiText.DynamicString("Couldn't fetch currency list"))
        } else {
            val latestCurrencies = hashMapOf<String, String>().apply {
                put("USD", "United states dollar")
                put("INR", "Indian Ruppee")
                put("AED", "United Arab Emirates Dirham")
                put("JPY", "Japanese Yen")
            }
            preferenceRepository.saveCountries(latestCurrencies)
            // for testing, subtracting 30 minutes from the current millis.
            preferenceRepository.saveTimeStamp(System.currentTimeMillis() - (1000 * 60 * 30))
            ApiResult.Success(latestCurrencies)
        }
    }

    @VisibleForTesting
    fun clearPreferences() {
        preferenceRepository.clearPreferences()
    }

}