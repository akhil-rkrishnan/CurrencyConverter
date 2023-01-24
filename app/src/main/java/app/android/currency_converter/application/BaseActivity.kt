package app.android.currency_converter.application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import app.android.currency_converter.presentation.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
* Base activity which is open for extension for all activity classes.
*/
@AndroidEntryPoint
open class BaseActivity : ComponentActivity() {

    // Injecting event bus from di [singleton]
    // we can go for multiple type of initialization based on requirement.
    // This can be initialized as lazy if it is not used by all classes
    @Inject
    lateinit var eventBus: EventBus


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // collecting a toast channel when activity is created.
        lifecycleScope.launch {
            eventBus.toastFlow.collectLatest {
                showToast(it)
            }
        }
    }
}