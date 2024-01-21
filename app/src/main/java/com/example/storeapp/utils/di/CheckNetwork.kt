package com.example.storeapp.utils.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import com.crazylegend.kotlinextensions.root.logError
import com.example.storeapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CheckNetwork {
    @Provides
    @Singleton
    fun provideCM(@ApplicationContext context: Context) =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    fun provideRequest(): NetworkRequest = NetworkRequest.Builder().apply {
        addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        //ANDROID M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            addCapability(NetworkCapabilities.NET_CAPABILITY_FOREGROUND)

        }
    }.build()

    @Provides
    @Singleton
    @Named(Constants.VPN_NETWORK_REQUEST)
    fun provideNrVpn(): NetworkRequest = NetworkRequest.Builder().apply {
        addTransportType(NetworkCapabilities.TRANSPORT_VPN)
        removeCapability(NetworkCapabilities.NET_CAPABILITY_NOT_VPN)
    }.build()

    @Provides
    @Singleton
    fun checkVpn(
        manager: ConnectivityManager,
        @Named(Constants.VPN_NETWORK_REQUEST) request: NetworkRequest
    ) = callbackFlow {

        val callBack = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                channel.trySend(true)
            }

            override fun onLost(network: Network) {
                channel.trySend(false)
            }

        }
        manager.registerNetworkCallback(request, callBack)
      awaitClose {
          manager.unregisterNetworkCallback(callBack) }
    }
}