package app.android.currency_converter.data.remote.convert

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ConversionResponse(
  @SerializedName("disclaimer") val disclaimer: String,
  @SerializedName("license") val license: String,
  @SerializedName("request") val request: Request,
  @SerializedName("meta") val meta: Meta,
  @SerializedName("response") val response: Int,
) {
    @Keep
    data class Request(
        @SerializedName("query") val disclaimer: String,
        @SerializedName("amount") val amount: Int,
        @SerializedName("from") val from: String,
        @SerializedName("to") val to: String,
    )
    @Keep
    data class Meta(
        @SerializedName("timestamp") val timestamp: Int,
        @SerializedName("rate") val rate: Int,
    )
}