package com.sopt.clody.domain.appupdate

import com.sopt.clody.domain.model.AppUpdateState

interface AppUpdateChecker {
    suspend fun getAppUpdateState(currentVersion: String): AppUpdateState
    suspend fun isUnderInspection(): Boolean
    suspend fun getInspectionTimeText(): Pair<String, String>?
}
