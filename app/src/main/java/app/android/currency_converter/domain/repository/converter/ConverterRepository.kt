package app.android.currency_converter.domain.repository.converter

import app.android.currency_converter.data.remote.convert.ConversionResponse
import app.android.currency_converter.data.remote.currency.LatestExchangeRateResponse
import app.android.currency_converter.data.utils.ApiResult

interface ConverterRepository {

    /**
    Since we are using a free account, this api can't be called using the free token.
    see [Free account restriction](https://docs.openexchangerates.org/reference/convert)

     * Method to convert currency from one country to another.
     * @param value inputted value by user
     * @param from selected base country
     * @param to country for conversion
     * @return ApiResult of conversion response
     * @see [value]
     */
    suspend fun convert(
        value: Double,
        from: String,
        to: String,
    ): ApiResult<ConversionResponse> = ApiResult.None

    /**
     * Method to get latest exchange rate from api
     * @param base_country inputted value by user
     * @param pretty_print set to false to reduce response size (removes whitespace)
     * @param show_alternative extend returned values with alternative, black market and digital currency rates
     * @return ApiResult of LatestExchangeRateResponse
     */
    suspend fun getLatestExchangeRate(
        base_country: String,
        pretty_print: Boolean,
        show_alternative: Boolean
    ): ApiResult<LatestExchangeRateResponse>

    /**
     * Method to get currencies from api
     * @param pretty_print set to false to reduce response size (removes whitespace)
     * @param show_alternative include alternative currencies.
     * @param show_inactive include historical/inactive currencies
     * @return ApiResult of currency map
     */
    suspend fun getCurrencies(
        pretty_print: Boolean,
        show_alternative: Boolean,
        show_inactive: Boolean,
    ): ApiResult<Map<String, String>>

}