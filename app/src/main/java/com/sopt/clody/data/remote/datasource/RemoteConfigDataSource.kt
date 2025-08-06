package com.sopt.clody.data.remote.datasource

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.sopt.clody.BuildConfig
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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

    fun getInspectionStart(): LocalDateTime? =
        remoteConfig.getString(KEY_INSPECTION_START).takeIf { it.isNotBlank() }?.let {
            runCatching { LocalDateTime.parse(it, formatter) }.getOrNull()
        }

    fun getInspectionEnd(): LocalDateTime? =
        remoteConfig.getString(KEY_INSPECTION_END).takeIf { it.isNotBlank() }?.let {
            runCatching { LocalDateTime.parse(it, formatter) }.getOrNull()
        }

    companion object {
        private const val KEY_LATEST_VERSION = "latest_version"
        private const val KEY_MINIMUM_VERSION = "min_required_version"
        private const val KEY_INSPECTION_START = "inspection_start_android"
        private const val KEY_INSPECTION_END = "inspection_end_android"

        private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    }
}
