package com.sopt.clody.data.remote.datasource

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.sopt.clody.BuildConfig
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfigDataSource @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig,
) {
    suspend fun fetch() {
        remoteConfig.fetchAndActivate().await()
    }

    fun getLatestVersion(): String =
        remoteConfig.getString(KEY_LATEST_VERSION).ifEmpty { BuildConfig.VERSION_NAME }

    fun getMinimumVersion(): String =
        remoteConfig.getString(KEY_MINIMUM_VERSION).ifEmpty { BuildConfig.VERSION_NAME }

    companion object {
        private const val KEY_LATEST_VERSION = "latest_version"
        private const val KEY_MINIMUM_VERSION = "minimum_version"
    }
}
