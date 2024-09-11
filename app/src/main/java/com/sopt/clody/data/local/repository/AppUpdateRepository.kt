package com.sopt.clody.data.local.repository

import com.google.android.play.core.appupdate.AppUpdateInfo

interface AppUpdateRepository {
    fun checkForUpdate(callback: (AppUpdateInfo?) -> Unit)
}
