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

    /**
     * Firebase RemoteConfig 로부터 점검 시간에 해당 하는지 검사하는 함수.
     *
     * @return 점검 시간 해당 여부
     * */
    override suspend fun isUnderInspection(): Boolean {
        val start = remoteConfigDataSource.getInspectionStart() ?: return false
        val end = remoteConfigDataSource.getInspectionEnd() ?: return false
        val serverZone = ZoneId.of(SERVER_TIMEZONE)

        val nowServer = ZonedDateTime.now(serverZone)
        val startZ = start.atZone(serverZone)
        val endZ = end.atZone(serverZone)

        return nowServer in startZ..endZ
    }

    /**
     * Firebase RemoteConfig 로부터 점검 시간을 가져와 반환하는 함수.
     *
     * @return 점검 시작 시간과 종료 시간을 "2025-08-11T18:00:00" 형식으로 반환
     * */
    override suspend fun getInspectionTimeText(): Pair<String, String>? {
        val start = remoteConfigDataSource.getInspectionStart() ?: return null
        val end = remoteConfigDataSource.getInspectionEnd() ?: return null
        return start.toString() to end.toString()
    }

    companion object {
        private const val SERVER_TIMEZONE = "Asia/Seoul"
    }
}
