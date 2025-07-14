package com.sopt.clody.data.remote.appupdate

import com.sopt.clody.data.remote.datasource.RemoteConfigDataSource
import com.sopt.clody.domain.appupdate.AppUpdateChecker
import com.sopt.clody.domain.model.AppUpdateState
import com.sopt.clody.domain.util.VersionComparator
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.Locale
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
        val now = LocalDateTime.now()
        return now.isAfter(start) && now.isBefore(end)
    }

    override fun getInspectionTimeText(): String? {
        val start = remoteConfigDataSource.getInspectionStart()
        val end = remoteConfigDataSource.getInspectionEnd()
        if (start == null || end == null) return null

        val startText = formatDateTimeWithDayOfWeek(start)
        val endText = formatDateTimeWithDayOfWeek(end)
        return "$startText ~ $endText"
    }

    private fun formatDateTimeWithDayOfWeek(dateTime: LocalDateTime): String {
        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)
        val month = dateTime.monthValue
        val day = dateTime.dayOfMonth
        val hour = dateTime.hour.toString().padStart(2, '0')

        return "$month/$day($dayOfWeek) ${hour}시"
    }
}
