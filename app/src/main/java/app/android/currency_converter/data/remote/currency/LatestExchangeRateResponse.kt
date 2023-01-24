package app.android.currency_converter.data.remote.currency


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class LatestExchangeRateResponse(
    @SerializedName("base") val base: String,
    @SerializedName("disclaimer") val disclaimer: String,
    @SerializedName("license") val license: String,
    @SerializedName("rates") val rates: Map<String, Double>,
    @SerializedName("timestamp") val timestamp: Long
)