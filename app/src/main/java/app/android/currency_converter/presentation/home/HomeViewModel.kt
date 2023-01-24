package app.android.currency_converter.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.android.currency_converter.application.EventBus
import app.android.currency_converter.data.constants.CURRENCY_INPUT
import app.android.currency_converter.data.constants.SELECTED_COUNTRY
import app.android.currency_converter.data.constants.TIMER_DELAY
import app.android.currency_converter.data.constants.TIMER_INTERVAL
import app.android.currency_converter.data.helpers.CurrencyTimer
import app.android.currency_converter.data.local.Country
import app.android.currency_converter.data.local.Currency
import app.android.currency_converter.data.utils.*
import app.android.currency_converter.domain.repository.converter.ConverterRepository
import app.android.currency_converter.domain.repository.localConversion.LocalConversionRepository
import app.android.currency_converter.domain.repository.preferences.PreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
/**
* ViewModel class for home screen
*/
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val converterRepository: ConverterRepository,
    private val preferenceRepository: PreferenceRepository,
    private val localConversionRepository: LocalConversionRepository,
    private val eventBus: EventBus,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var apiTimer: CurrencyTimer? =
        CurrencyTimer(delay = TIMER_DELAY, durationInMillis = TIMER_INTERVAL, tasks = {
            fetchOrLoadLocally()
        })

    val currentCurrencyInput = savedStateHandle.getStateFlow(CURRENCY_INPUT, "")
    val currentBaseCountry = savedStateHandle.getStateFlow(SELECTED_COUNTRY, "USD")
    val countryList = MutableStateFlow(listOf<Country>())
    val latestExchangeRateList = MutableStateFlow(listOf<Currency>())

    private var _isLoading by mutableStateOf(false)
    val isLoading get() = _isLoading

    init {
        loaderCollector()
        fetchOrLoadLocally()
        combineInputEvents()
    }

    /**
    * Method to combine the currency input and select country state flows.
    */
    private fun combineInputEvents() {
        currentCurrencyInput.combine(currentBaseCountry) { currencyInput, baseCountry ->
            if (latestExchangeRateList.value.isNotEmpty()) {
                performLocalConversionWithData(
                    baseCountry = baseCountry,
                    currencyInput = currencyInput.assignDefaultIfNotValid()
                )
            }
        }.launchIn(viewModelScope)

    }
    /**
    * Method to update the currency input
     * @param value latest currency input value
    */
    fun onCurrencyInputChange(value: String) {
        savedStateHandle[CURRENCY_INPUT] = value
    }

    /**
    * Method to update the selected country
     * @param value country selected from drop down menu
    */
    fun onCountrySelected(value: Country) {
        savedStateHandle[SELECTED_COUNTRY] = value.code
    }

    /**
    * Method to update the loading state
     * @param isLoading latest loading value
    */
    private fun updateLoading(isLoading: Boolean) {
        _isLoading = isLoading
    }

    /**
     * Method to clear the loader
     */
    private fun loaderCollector() {
        viewModelScope.launch {
            latestExchangeRateList.collect {
                updateLoading(false)
            }
        }
    }

    /**
     * Method to get the currency country map json
     */
    private fun getCountries() {

        viewModelScope.launch {
            val response = converterRepository.getCurrencies(
                pretty_print = false,
                show_alternative = false,
                show_inactive = false
            )

            response.ifSuccess { map ->
                countryList.update {
                    map.asCountries()
                }
            }

            response.ifFailed { message ->
                eventBus.sendToast(message)
            }
        }
    }

    /**
     * Method to get the latest exchange rates
     * @param baseCountry Country selected from the drop down
     */
    private fun getLatestExchangeRates(baseCountry: String) {
        updateLoading(true)
        viewModelScope.launch {
            val response =
                converterRepository.getLatestExchangeRate(
                    base_country = baseCountry,
                    pretty_print = false,
                    show_alternative = false
                )

            response.ifSuccess { exchangeRateResponse ->
                latestExchangeRateList.update {
                    exchangeRateResponse.rates.asExchangeRateList()
                }
                // timer will be scheduled once latest exchange response is received. It schedule only once.
                apiTimer?.scheduleAndStart()
            }

            response.ifFailed { message ->
                eventBus.sendToast(message)
                updateLoading(false)
            }
        }
    }

    /**
    * Method to load the data from api / preference
    */
    fun fetchOrLoadLocally() {
        updateLoading(true)
        viewModelScope.launch {
            launch {
                loadCountriesFromPref().let { countries ->
                    // don't need to refresh this currency list, since it only contains the Country name and currency codes.
                    if (countries == null) {
                        getCountries()
                    } else {
                        // updating data from preference
                        countryList.update {
                            countries
                        }
                    }
                }
            }
            launch {
                loadExchangeRateFromPref().let { exchangeRate ->
                    if (exchangeRate == null || loadLastSavedTimeStampFromPref().needsRefresh()
                    ) {
                        getLatestExchangeRates(baseCountry = exchangeRate?.base ?: "USD")
                    } else {
                        // updating data from preference
                        latestExchangeRateList.update {
                            exchangeRate.rates.asExchangeRateList()
                        }
                    }
                }
            }
        }
    }

    /**
     * Method to load countries from preferences
     * @return List of countries from preference
     */
    fun loadCountriesFromPref() = preferenceRepository.loadCountries()

    /**
     * Method to load countries from latest exchange rate from preference
     * @return Latest exchange rate from preference
     */
    fun loadExchangeRateFromPref() = preferenceRepository.loadLatestExchangeRate()

    /**
     * Method to load last saved time stamp from preference
     * @return Last saved time stamp from preference
     */
    fun loadLastSavedTimeStampFromPref() = preferenceRepository.getLastSavedTimestamp()

    /**
    * Method to perform the currency conversion based on the input
     * @param currencyInput Currency value inputted by the user
     * @param baseCountry Country selected from drop down menu by the user
    */
    fun performLocalConversionWithData(currencyInput: String, baseCountry: String) {
        //loading the exchange rate from prefs
        loadExchangeRateFromPref()?.rates?.asExchangeRateList()?.let {
            val updatedList = localConversionRepository.convertCurrencies(
                baseCountry = baseCountry,
                currencyInput = currencyInput,
                latestExchangeRates = it
            )
            if (updatedList.isNotEmpty()) {
                latestExchangeRateList.update {
                    updatedList
                }
            }
        }

    }

    /**
    * Method to get the country name from currency list
     * @param code Currency code of particular country
     * @return Country name from the currency list (Loaded from preference).
    */
    fun getCountryName(code: String): String {
        return preferenceRepository.getCountryName(code)
    }

    /**
    * Method to clear the api timer when view model is cleared.
    */
    override fun onCleared() {
        super.onCleared()
        apiTimer?.stopTimer()
    }
}