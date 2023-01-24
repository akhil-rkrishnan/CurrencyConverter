package app.android.currency_converter.domain.repository.converter

import app.android.currency_converter.BuildConfig
import app.android.currency_converter.data.remote.CurrencyConverterApi
import app.android.currency_converter.data.utils.ApiResult
import app.android.currency_converter.data.utils.initApiCall
import app.android.currency_converter.domain.repository.preferences.PreferenceRepository

/**
* Currency Converter Repository implementation
*/
class ConverterRepositoryImpl(
    private val currencyConverterApi: CurrencyConverterApi,
    private val preferenceRepository: PreferenceRepository,
) : ConverterRepository {


    override suspend fun convert(
        value: Double,
        from: String,
        to: String,
    ) = initApiCall {
        ApiResult.Success(
            currencyConverterApi.convert(
                value = value,
                from = from,
                to = to,
                app_id = BuildConfig.EXCHANGE_RATE_APPID
            )
        )
    }

    override suspend fun getLatestExchangeRate(
        base_country: String,
        pretty_print: Boolean,
        show_alternative: Boolean
    ) = initApiCall {
        val response = currencyConverterApi.getLatestExchangeRate(
            app_id = BuildConfig.EXCHANGE_RATE_APPID,
            pretty_print = pretty_print,
            show_alternative = show_alternative,
            base_country = base_country
        )

        preferenceRepository.saveLatestExchangeRate(response)
        // saving the last fetch time to perform refreshing. Not using timestamp from the server to avoid region locale issues.
        preferenceRepository.saveTimeStamp(System.currentTimeMillis())
        ApiResult.Success(response)
    }

    override suspend fun getCurrencies(
        pretty_print: Boolean,
        show_alternative: Boolean,
        show_inactive: Boolean,
    ) =
        initApiCall {
            val response = currencyConverterApi.getCurrencies(
                pretty_print = pretty_print,
                show_alternative = show_alternative,
                show_inactive = show_inactive
            )
            preferenceRepository.saveCountries(response)
            ApiResult.Success(response)
        }


}