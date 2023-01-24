package app.android.currency_converter.data.local

/**
 * Local conversion currency class, which can be injected to DI if this class is used in several places in future.
 */
class LocalConversion {

    // Fall back hash map if the selected base country in not available in the openexchange api
    private val fallBackCurrencyMap by lazy {
        hashMapOf<String, Double>().apply {
            put("VEF", 19.959523)
        }
    }

    /**
     * Method to convert the currencies with respect to the data retrieved from openexchange api
     */
    fun convertCurrencies(
        baseCountry: String,
        currencyInput: String,
        latestExchangeRates: List<Currency>
    ): List<Currency> {
        val conversionValue = latestExchangeRates.find { it.code == baseCountry }?.rate
        // if conversionValue is null, then base country is not available in the response.
        return if (conversionValue != null) {
            processMap(conversionValue, currencyInput, latestExchangeRates)
        } else {
            convertCurrencyWithDefault(
                baseCountry = baseCountry,
                currencyInput = currencyInput,
                latestExchangeRates = latestExchangeRates
            )
        }
    }

    /**
     * Method to load the missing currencies in openexchange api.
     */
    private fun convertCurrencyWithDefault(
        baseCountry: String,
        currencyInput: String,
        latestExchangeRates: List<Currency>
    ): List<Currency> {
        val defaultConversionValue = fallBackCurrencyMap[baseCountry]
        return processMap(defaultConversionValue, currencyInput, latestExchangeRates)
    }

    /**
     * Method to map the updated rates
     */
    private fun processMap(
        conversionValue: Double?,
        currencyInput: String?,
        latestExchangeRates: List<Currency>
    ): List<Currency> {
        try {
            if (conversionValue == null || currencyInput.isNullOrEmpty()) {
                return emptyList()
            }
            latestExchangeRates.let { exchangeRates ->
                // updated exchange rate based on the input base country
                return exchangeRates.map { entry ->
                    Currency(entry.code, (entry.rate / conversionValue) * currencyInput.toDouble())
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return emptyList()
        }
    }
}