package com.sopt.clody.data.remote.appupdate

import com.sopt.clody.data.remote.datasource.RemoteConfigDataSource
import com.sopt.clody.domain.appupdate.AppUpdateChecker
import com.sopt.clody.domain.model.AppUpdateState
import com.sopt.clody.domain.util.VersionComparator
import javax.inject.Inject

class AppUpdateCheckerImpl @Inject constructor(
    private val remoteConfigDataSource: RemoteConfigDataSource,
) : AppUpdateChecker {

    override suspend fun getAppUpdateState(currentVersion: String): AppUpdateState {
        remoteConfigDataSource.fetch()

        val latestVersion = remoteConfigDataSource.getLatestVersion()
        val minimumVersion = remoteConfigDataSource.getMinimumVersion()

        return when {
            VersionComparator.compare(currentVersion, minimumVersion) < 0 -> {
                AppUpdateState.HardUpdate(latestVersion)
            }

            VersionComparator.compare(currentVersion, latestVersion) < 0 -> {
                AppUpdateState.SoftUpdate(latestVersion)
            }

            else -> {
                AppUpdateState.Latest
            }
        }
    }
}
