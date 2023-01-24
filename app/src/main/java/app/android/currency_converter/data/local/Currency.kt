package app.android.currency_converter.data.local

import androidx.annotation.Keep

@Keep
data class Currency(
    val code: String,
    val rate: Double,
)

@Keep
data class Country(
    val code: String,
    val name: String
)