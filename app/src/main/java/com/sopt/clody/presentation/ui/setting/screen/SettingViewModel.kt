package com.sopt.clody.presentation.ui.setting.screen

import android.content.Context
import android.content.pm.PackageInfo
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    var versionInfo by mutableStateOf(APP_VERSION)
        private set

    fun getVersionInfo() {
        val info: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        versionInfo = info.versionName
    }

    companion object {
        const val APP_VERSION = "최신버전"
    }
}
