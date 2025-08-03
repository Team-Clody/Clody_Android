package com.sopt.clody.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

/**
 * 네트워크 연결 상태를 관찰하는 Observer.
 *
 * - `Available`: 인터넷에 연결되어 있음
 * - `Unavailable`: 인터넷 연결이 끊긴 상태
 *
 */
class NetworkConnectivityObserver @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /**
     * 네트워크 상태를 실시간으로 스트리밍하는 Flow.
     *
     * - 최초 구독 시 현재 상태를 먼저 전송 ->
     * - 이후 네트워크 변경 이벤트를 수신하여 상태를 전송
     * - 중복 상태 전송은 [distinctUntilChanged]로 방지 하도록 함.
     */
    val networkStatus: Flow<NetworkStatus> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {

            /**
             * 네트워크가 변경되었을 때 호출됨.
             * 유효한 인터넷 연결이 있는지 확인하여 상태를 전송.
             */
            override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
                val hasInternet = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                trySend(if (hasInternet) NetworkStatus.Available else NetworkStatus.Unavailable)
            }

            /**
             * 네트워크 연결이 완전히 끊겼을 때 호출.
             */
            override fun onLost(network: Network) {
                trySend(NetworkStatus.Unavailable)
            }
        }

        trySend(if (isCurrentlyAvailable()) NetworkStatus.Available else NetworkStatus.Unavailable)

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged()

    /**
     * 현재 활성 네트워크가 인터넷에 연결되어 있는지를 반환
     *
     * @return 인터넷 연결 여부
     */
    private fun isCurrentlyAvailable(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}
