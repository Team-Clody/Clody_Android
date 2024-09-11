package com.sopt.clody.data.local.repositoryimpl

import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.sopt.clody.data.local.repository.AppUpdateRepository
import javax.inject.Inject

class AppUpdateRepositoryImpl @Inject constructor
    (private val appUpdateManager: AppUpdateManager) : AppUpdateRepository {
    override fun checkForUpdate(callback: (AppUpdateInfo?) -> Unit) {
        val appUpdateInfoTask: Task<AppUpdateInfo> = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            callback(appUpdateInfo)
        }.addOnFailureListener {
            callback(null)
        }
    }
}
