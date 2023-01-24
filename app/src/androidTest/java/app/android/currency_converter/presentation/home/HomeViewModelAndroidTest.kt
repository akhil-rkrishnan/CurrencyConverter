package app.android.currency_converter.presentation.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.test.filters.MediumTest
import app.android.currency_converter.MainDispatcherRule
import app.android.currency_converter.application.EventBus
import app.android.currency_converter.data.constants.REFRESH_TIME
import app.android.currency_converter.data.preferences.SharedPreferenceService
import app.android.currency_converter.di.TestEventBus
import app.android.currency_converter.di.TestLocalConversionRepository
import app.android.currency_converter.di.TestPreferenceRepository
import app.android.currency_converter.di.TestSharedPreferenceService
import app.android.currency_converter.domain.repository.FakeConverterRepository
import app.android.currency_converter.domain.repository.localConversion.LocalConversionRepository
import app.android.currency_converter.domain.repository.preferences.PreferenceRepository
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

/**
* Home view mode test class. Testing hilt injection
*/
@MediumTest
@HiltAndroidTest
class HomeViewModelAndroidTest {

    @get: Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @ExperimentalCoroutinesApi
    @get: Rule
    val mainCoroutineRule = MainDispatcherRule()

    @get: Rule
    val instantTaskExecuterRule = InstantTaskExecutorRule()

    @Inject
    @TestSharedPreferenceService
    lateinit var preferenceService: SharedPreferenceService

    private lateinit var fakeRepository: FakeConverterRepository

    @Inject
    @TestPreferenceRepository
    lateinit var preferenceRepository: PreferenceRepository

    @Inject
    @TestLocalConversionRepository
    lateinit var localConversionRepository: LocalConversionRepository

    @Inject
    @TestEventBus
    lateinit var eventBus: EventBus


    private lateinit var viewModel: HomeViewModel


    @Before
    fun setup() {
        hiltRule.inject()

        fakeRepository = FakeConverterRepository(
            preferenceRepository = preferenceRepository,
            localConversionRepository = localConversionRepository
        )

        viewModel = HomeViewModel(
            converterRepository = fakeRepository,
            savedStateHandle = SavedStateHandle(emptyMap()),
            preferenceRepository = preferenceRepository,
            localConversionRepository = localConversionRepository,
            eventBus = eventBus
        )
    }

    /**
    * Testing data from preference
    */
    @Test
    fun checkLocalCurrencyAndExchangeRate_isNull() = runTest {
        //checking country map from preferences
        assertThat(viewModel.loadCountriesFromPref()).isNull()
        //checking latest exchange rate from preferences
        assertThat(viewModel.loadExchangeRateFromPref()).isNull()
    }

    /**
     * Testing data from preference
     */
    @Test
    fun checkLocalCurrency_isNotNull() = runTest {
        // fetching country from api method initially and saving to preferences
        advanceUntilIdle()
        assertThat(viewModel.loadCountriesFromPref()).isNotNull()
    }

    /**
     * Testing data from preference
     */
    @Test
    fun checkLatestExchangeRate_isNotNull() = runTest {
        // fetching latest exchange rate from api method initially and saving to preferences
        advanceUntilIdle()
        assertThat(viewModel.loadExchangeRateFromPref()).isNotNull()
    }

    /**
     * Testing data from preference
     */
    @Test
    fun checkLastSavedTimeStamp_isNotNull() = runTest {
        // fetching last saved time stamp from preferences after api call
        advanceUntilIdle()
        assertThat(viewModel.loadExchangeRateFromPref())
    }

    @Test
    fun checkLastSavedTimeStamp_needsRefresh_isTrue() = runTest {
        advanceUntilIdle()
        val lastSavedTimeStamp = viewModel.loadLastSavedTimeStampFromPref()
        assertThat(lastSavedTimeStamp).isNotNull()
        val currentTime = System.currentTimeMillis()
        val minutes = (currentTime - lastSavedTimeStamp!!) / (1000 * 60)
        assertThat(minutes).isEqualTo(REFRESH_TIME)
    }

    @After
    fun finish() {
        fakeRepository.clearPreferences()
    }
}