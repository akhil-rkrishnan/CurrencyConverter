package app.android.currency_converter.data.remote.error

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ErrorResponse(
    @SerializedName("error") val error: Boolean,
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("description") val description: Boolean,
    )