package com.example.storeapp.utils.network

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@Suppress("DEPRECATION")
class NetworkChecker @Inject constructor(
    private val manager: ConnectivityManager,
    private val request: NetworkRequest
) :
    ConnectivityManager.NetworkCallback() {
    private val isNetworkAvailable = MutableStateFlow(false)
    private var capabilities: NetworkCapabilities? = null

    fun checkNetwork(): MutableStateFlow<Boolean> {

        manager.registerNetworkCallback(request, this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = manager.activeNetwork
            capabilities = manager.getNetworkCapabilities(network)
            if (network == null) {

                isNetworkAvailable.value = false
                return isNetworkAvailable
            }
            if (capabilities == null) {
                isNetworkAvailable.value = false
                return isNetworkAvailable
            }
            isNetworkAvailable.value = when {
                capabilities!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities!!.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                else -> false
            }

        } else {
            manager.run {
                manager.activeNetworkInfo?.apply {
                    isNetworkAvailable.value = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        else -> false
                    }
                }
            }
        }
        return isNetworkAvailable

    }


    override fun onAvailable(network: Network) {
        isNetworkAvailable.value = true
    }

    override fun onLost(network: Network) {
        isNetworkAvailable.value = false
    }
}