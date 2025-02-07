package com.sopt.clody.data.remote.util

import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class NetworkUtil(private val connectivityManager: ConnectivityManager) {

    fun isNetworkAvailable(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
