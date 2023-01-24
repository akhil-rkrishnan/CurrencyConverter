package app.android.currency_converter.domain.repository

import app.android.currency_converter.data.remote.convert.ConversionResponse
import app.android.currency_converter.data.remote.currency.LatestExchangeRateResponse
import app.android.currency_converter.data.utils.ApiResult
import app.android.currency_converter.domain.repository.converter.ConverterRepository
import app.android.currency_converter.presentation.utils.UiText

/**
* Fake converter repository class
*/
class FakeConverterRepository : ConverterRepository {

    private var shouldReturnError: Boolean = false

    override suspend fun convert(
        value: Double,
        from: String,
        to: String
    ): ApiResult<ConversionResponse> {
        return if (shouldReturnError) {
            ApiResult.Failed(UiText.DynamicString("Currency conversion error"))
        } else {
            ApiResult.Success(
                ConversionResponse(
                    "disclaimer", "license", ConversionResponse.Request(
                        "disclaimer", 123, "from", "to"
                    ), ConversionResponse.Meta(timestamp = 16543213, rate = 100), 200
                )
            )
        }

    }

    override suspend fun getLatestExchangeRate(
        base_country: String,
        pretty_print: Boolean,
        show_alternative: Boolean
    ): ApiResult<LatestExchangeRateResponse> {
        return if (shouldReturnError) {
            ApiResult.Failed(UiText.DynamicString("Couldn't fetch latest exchange rate"))
        } else {
            // rates are mentioned based on USD rate
            ApiResult.Success(
                LatestExchangeRateResponse(
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
            )
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
            // rates are mentioned based on USD rate
            ApiResult.Success(hashMapOf<String, String>().apply {
                put("USD", "United states dollar")
                put("INR", "Indian Ruppee")
                put("AED", "United Arab Emirates Dirham")
                put("JPY", "Japanese Yen")
            })
        }
    }

}