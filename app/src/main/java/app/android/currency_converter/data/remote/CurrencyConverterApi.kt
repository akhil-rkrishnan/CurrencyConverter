package app.android.currency_converter.data.remote

import app.android.currency_converter.data.remote.convert.ConversionResponse
import app.android.currency_converter.data.remote.currency.LatestExchangeRateResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CurrencyConverterApi {

    @GET("api/latest.json")
    suspend fun getLatestExchangeRate(
        @Query("app_id") app_id: String,
        @Query("base") base_country: String,
        @Query("prettyprint") pretty_print: Boolean,
        @Query("show_alternative") show_alternative: Boolean
    ): LatestExchangeRateResponse

    @GET("api/currencies.json")
    suspend fun getCurrencies(
        @Query("prettyprint") pretty_print: Boolean,
        @Query("show_alternative") show_alternative: Boolean,
        @Query("show_inactive") show_inactive: Boolean
    ): Map<String, String>

    @GET("api/convert/{value}/{from}/{to}")
    suspend fun convert(
        @Path("value") value: Double,
        @Path("from") from: String,
        @Path("to") to: String,
        @Query("app_id") app_id: String
    ): ConversionResponse
}