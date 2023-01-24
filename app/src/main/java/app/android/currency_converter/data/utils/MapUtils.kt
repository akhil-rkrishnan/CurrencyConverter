package app.android.currency_converter.data.utils

import app.android.currency_converter.data.local.Country
import app.android.currency_converter.data.local.Currency

/**
* Method to transform the currency map
 * @return list of countries sorted by name
*/
fun Map<String, String>.asCountries() : List<Country> {
    return this.map { Country(code = it.key, name = it.value) }.sortedBy { it.name }
}

/**
* Method to transform rate map
 * @return list of currencies sorted by currency code
*/
fun Map<String, Double>.asExchangeRateList() : List<Currency> {
    return this.map { Currency(code = it.key, rate = it.value) }.sortedBy { it.code }
}