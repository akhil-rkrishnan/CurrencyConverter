package app.android.currency_converter.data.helpers

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import app.android.currency_converter.data.utils.isInternetAvailable

/**
* Class for network connection handle.
*/
class NetworkConnection(private val context: Context) : ConnectivityManager.NetworkCallback() {

    private val connectivityManager by lazy {
        context.getSystemService(ConnectivityManager::class.java)
    }

    private val networkRequest by lazy {
        NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
    }

    private var _isNetworkAvailable by mutableStateOf(false)
    val isNetworkAvailable get() = _isNetworkAvailable

    init {
        connectivityManager.requestNetwork(networkRequest, this)
    }

    // network is available for use
    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        _isNetworkAvailable = network.isInternetAvailable()
    }


    // lost network connection
    override fun onLost(network: Network) {
        super.onLost(network)
        _isNetworkAvailable = false
    }
}



