package com.sopt.clody.data.remote.datasource

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
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

    fun getLatestVersion(): String = remoteConfig.getString("latest_version")
    fun getMinimumVersion(): String = remoteConfig.getString("min_required_version")
}
