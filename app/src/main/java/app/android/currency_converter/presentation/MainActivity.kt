package app.android.currency_converter.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import app.android.currency_converter.application.BaseActivity
import app.android.currency_converter.core.theme.CurrencyConverterTheme
import app.android.currency_converter.core.theme.black
import app.android.currency_converter.data.helpers.NetworkConnection
import app.android.currency_converter.presentation.home.HomeScreen
import app.android.currency_converter.presentation.home.HomeViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val viewModel by viewModels<HomeViewModel>()

    // injecting variable for network state
    @Inject
    lateinit var networkModule: NetworkConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyConverterTheme {
                val systemUiController = rememberSystemUiController()
                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = black,
                        darkIcons = false
                    )
                }
                Surface(modifier = Modifier
                    .fillMaxSize(), color = MaterialTheme.colors.background) {
                    HomeScreen(viewModel, networkModule.isNetworkAvailable)
                }
            }
        }
    }
}
