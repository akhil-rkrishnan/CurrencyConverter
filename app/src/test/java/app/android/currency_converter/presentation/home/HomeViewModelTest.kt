package app.android.currency_converter.presentation.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import app.android.currency_converter.MainDispatcherRule
import app.android.currency_converter.application.EventBus
import app.android.currency_converter.data.local.Country
import app.android.currency_converter.data.local.LocalConversion
import app.android.currency_converter.domain.repository.FakeConverterRepository
import app.android.currency_converter.domain.repository.FakeLocalConversionRepository
import app.android.currency_converter.domain.repository.FakePreferenceRepository
import app.android.currency_converter.domain.repository.converter.ConverterRepository
import app.android.currency_converter.domain.repository.localConversion.LocalConversionRepository
import app.android.currency_converter.domain.repository.preferences.PreferenceRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.text.DecimalFormat

/**
* Home view model test class
*/
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecuterRule = InstantTaskExecutorRule()


    private lateinit var converterRepository: ConverterRepository
    private lateinit var preferenceRepository: PreferenceRepository
    private lateinit var localRepository: LocalConversionRepository
    private lateinit var eventBus: EventBus

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        converterRepository = FakeConverterRepository()
        preferenceRepository = FakePreferenceRepository()
        localRepository = FakeLocalConversionRepository(LocalConversion())
        eventBus = EventBus()
        viewModel = HomeViewModel(
            savedStateHandle = SavedStateHandle(emptyMap()),
            converterRepository = converterRepository,
            preferenceRepository = preferenceRepository,
            localConversionRepository = localRepository,
            eventBus = eventBus
        )
    }

    @Test
    fun `currency and country input default value returns true`() = runTest {
        assertThat(viewModel.currentCurrencyInput.value).isEmpty()
        assertThat(viewModel.currentBaseCountry.value).isEqualTo("USD")
    }

    @Test
    fun `empty currency list returns true`() = runTest {
        assertThat(viewModel.countryList.value).isEmpty()
    }

    @Test
    fun `empty exchange rate list returns true`() = runTest {
        assertThat(viewModel.latestExchangeRateList.value).isEmpty()
    }

    @Test
    fun `valid currency and country input returns true`() = runTest {
        val currentInput = "141"
        val currentCountryCode = "USD"
        val currentSelectedCountry = Country(currentCountryCode, "$currentCountryCode money")

        viewModel.onCurrencyInputChange(currentInput)
        viewModel.onCountrySelected(currentSelectedCountry)

        advanceUntilIdle()

        assertThat(viewModel.currentCurrencyInput.value).isEqualTo(currentInput)
        assertThat(viewModel.currentBaseCountry.value).isEqualTo(currentCountryCode)
    }

    @Test
    fun `valid rate conversion returns true`() = runTest {
        val currentInput = "141"
        val currentCountryCode = "USD"
        val currentSelectedCountry = Country(currentCountryCode, "$currentCountryCode rate")

        viewModel.onCurrencyInputChange(currentInput)
        viewModel.onCountrySelected(currentSelectedCountry)

        advanceUntilIdle()

        val toCountryCode = "INR"
        //will be null if code is not available
        val toCountryRate =
            viewModel.latestExchangeRateList.value.find { it.code == toCountryCode }?.rate
        //will be null if code is not available
        val fromCountryRate =
            viewModel.latestExchangeRateList.value.find { it.code == viewModel.currentBaseCountry.value }?.rate

        advanceUntilIdle()

        assertThat(toCountryRate).isNotNull()
        assertThat(fromCountryRate).isNotNull()
        val conversion =
            (toCountryRate!! / fromCountryRate!!) * viewModel.currentCurrencyInput.value.toDouble()
        viewModel.performLocalConversionWithData(
            currencyInput = viewModel.currentCurrencyInput.value,
            baseCountry = viewModel.currentBaseCountry.value
        )

        advanceUntilIdle()

        val updatedToValue =
            viewModel.latestExchangeRateList.value.find { it.code == toCountryCode }?.rate
        assertThat(updatedToValue).isNotNull()
        assertThat(conversion).isEqualTo(updatedToValue)
    }

    @Test
    fun `valid rate conversion from Japanese Yen to Indian Rupee by literal returns true`() =
        runTest {
            val currentInput = "141"
            val currentCountryCode = "JPY"
            val currentSelectedCountry = Country(currentCountryCode, "$currentCountryCode rate")

            viewModel.onCurrencyInputChange(currentInput)
            viewModel.onCountrySelected(currentSelectedCountry)

            advanceUntilIdle()

            val toCountryCode = "INR"
            //will be null if code is not available
            val toCountryRate =
                viewModel.latestExchangeRateList.value.find { it.code == toCountryCode }?.rate
            //will be null if code is not available
            val fromCountryRate =
                viewModel.latestExchangeRateList.value.find { it.code == viewModel.currentBaseCountry.value }?.rate

            advanceUntilIdle()

            assertThat(toCountryRate).isNotNull()
            assertThat(fromCountryRate).isNotNull()
            //literal value:  1 -> INR, 1.60 -> Japanese Yen, 141 -> Current currency input
            val conversion = ((1 / 1.60) * 141).let {
                DecimalFormat("#.2f").format(it).toDouble()
            }
            viewModel.performLocalConversionWithData(
                currencyInput = viewModel.currentCurrencyInput.value,
                baseCountry = viewModel.currentBaseCountry.value
            )

            advanceUntilIdle()

            val updatedToValue =
                viewModel.latestExchangeRateList.value.find { it.code == toCountryCode }?.rate?.let {
                    DecimalFormat("#.2f").format(it).toDouble()
                }
            assertThat(updatedToValue).isNotNull()
            assertThat(conversion).isEqualTo(updatedToValue)
        }


    @Test
    fun `rate conversion of all currencies in the list returns true`() = runTest {
        val currentInput = "141"
        val currentCountryCode = "INR"
        val currentSelectedCountry = Country(currentCountryCode, "$currentCountryCode rate")
        advanceUntilIdle()
        val listBeforeConversion = viewModel.latestExchangeRateList.value
        // ensuring list is not empty
        assertThat(listBeforeConversion).isNotEmpty()
        viewModel.onCurrencyInputChange(currentInput)
        viewModel.onCountrySelected(currentSelectedCountry)
        assertThat(viewModel.currentCurrencyInput.value).isEqualTo(currentInput)
        assertThat(viewModel.currentBaseCountry.value).isEqualTo(currentCountryCode)
        val rate =
            viewModel.latestExchangeRateList.value.find { it.code == viewModel.currentBaseCountry.value }?.rate
        assertThat(rate).isNotNull()
        viewModel.performLocalConversionWithData(
            currencyInput = viewModel.currentCurrencyInput.value,
            baseCountry = viewModel.currentBaseCountry.value
        )
        for (item in viewModel.latestExchangeRateList.value) {
            val valueBeforeRefresh = listBeforeConversion.find { it.code == item.code }?.rate
            assertThat(valueBeforeRefresh).isNotNull()
            val conversion =
                valueBeforeRefresh!! / rate!! * viewModel.currentCurrencyInput.value.toDouble()
            assertThat(conversion).isEqualTo(item.rate)
        }
    }

    @Test
    fun `currency not available local conversion gives same list returns true`() = runTest {

        advanceUntilIdle()
        val currentInput = "100"
        val unknownCode = "XYZ"
        val unknownCountry = Country(code = unknownCode, name = "Unknown currency")
        val oldList = viewModel.latestExchangeRateList.value
        viewModel.onCurrencyInputChange(currentInput)
        viewModel.onCountrySelected(unknownCountry)
        advanceUntilIdle()
        val newList = viewModel.latestExchangeRateList.value
        assertThat(oldList).isEqualTo(newList)

    }
    @Test
    fun `currency not available local conversion gives new rate list calculated based on fallback value returns true`() = runTest {

        advanceUntilIdle()
        val currentInput = "100"
        val invalidCurrencyInOpenExchange = "VEF" // not available in open exchange rate
        val unknownCountry = Country(code = invalidCurrencyInOpenExchange, name = "Unknown currency")
        val listBeforeConversion = viewModel.latestExchangeRateList.value
        viewModel.onCurrencyInputChange(currentInput)
        viewModel.onCountrySelected(unknownCountry)
        advanceUntilIdle()
        val updatedList = viewModel.latestExchangeRateList.value
        updatedList.forEach {item ->
            val valueBeforeRefresh = listBeforeConversion.find { it.code == item.code }?.rate
            assertThat(valueBeforeRefresh).isNotNull()
            assertThat((valueBeforeRefresh!! / 19.959523)*currentInput.toDouble()).isEqualTo(item.rate)
        }

    }

    @Test
    fun `invalid currency code returns true`() = runTest {
        val currencyCode = "MNO"
        val selectedCountry = Country(code = currencyCode, name = "Indian ruppee")
        viewModel.onCountrySelected(selectedCountry)
        advanceUntilIdle()
        val item = viewModel.countryList.value.find { it.code == currencyCode }
        advanceUntilIdle()
        assertThat(item).isNull()
    }


}