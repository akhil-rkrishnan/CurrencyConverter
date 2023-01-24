package app.android.currency_converter.domain.repository.localConversion

import app.android.currency_converter.data.local.Currency

interface LocalConversionRepository {
    /**
    * Method to convert currencies
     * @param baseCountry selected base country
     * @param currencyInput inputted currency value
     * @param latestExchangeRates latest exchange rate
     * @return list of updated currencies based on the input
    */
    fun convertCurrencies(baseCountry: String, currencyInput: String, latestExchangeRates: List<Currency>): List<Currency>
}