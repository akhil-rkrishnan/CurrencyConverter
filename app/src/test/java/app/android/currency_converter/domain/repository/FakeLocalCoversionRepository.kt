package app.android.currency_converter.domain.repository

import app.android.currency_converter.data.local.Currency
import app.android.currency_converter.data.local.LocalConversion
import app.android.currency_converter.domain.repository.localConversion.LocalConversionRepository

/**
* Fake Local conversion repository
 * @property localConversion Local conversion class
*/
class FakeLocalConversionRepository(private val localConversion: LocalConversion) :
    LocalConversionRepository {

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