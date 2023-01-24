package app.android.currency_converter.domain.repository.localConversion

import app.android.currency_converter.data.local.Currency
import app.android.currency_converter.data.local.LocalConversion

/**
* Local conversion repository implementation
*/
class LocalConversionRepositoryImpl(
    private val localConversion: LocalConversion
) : LocalConversionRepository {
    override fun convertCurrencies(
        baseCountry: String,
        currencyInput: String,
        latestExchangeRates: List<Currency>
    ): List<Currency> {
        return localConversion.convertCurrencies(
            baseCountry = baseCountry,
            currencyInput = currencyInput,
            latestExchangeRates = latestExchangeRates
        )
    }
}