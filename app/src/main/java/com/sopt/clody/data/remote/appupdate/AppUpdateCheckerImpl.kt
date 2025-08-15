package com.sopt.clody.data.remote.appupdate

import com.sopt.clody.data.remote.datasource.RemoteConfigDataSource
import com.sopt.clody.domain.appupdate.AppUpdateChecker
import com.sopt.clody.domain.model.AppUpdateState
import com.sopt.clody.domain.util.VersionComparator
import java.time.ZoneId
import java.time.ZonedDateTime
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

    override suspend fun isUnderInspection(): Boolean {
        val start = remoteConfigDataSource.getInspectionStart() ?: return false
        val end = remoteConfigDataSource.getInspectionEnd() ?: return false
        val serverZone = ZoneId.of("Asia/Seoul")

        val nowServer = ZonedDateTime.now(serverZone)
        val startZ = start.atZone(serverZone)
        val endZ = end.atZone(serverZone)

        return nowServer.isAfter(startZ) && nowServer.isBefore(endZ)
    }

    override suspend fun getInspectionTimeText(): Pair<String, String>? {
        val start = remoteConfigDataSource.getInspectionStart() ?: return null
        val end = remoteConfigDataSource.getInspectionEnd() ?: return null
        return start.toString() to end.toString()
    }
}
