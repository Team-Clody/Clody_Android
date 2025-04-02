package com.sopt.clody.domain.model

sealed interface AppUpdateState {
    data object Latest : AppUpdateState
    data class SoftUpdate(val latestVersion: String) : AppUpdateState
    data class HardUpdate(val latestVersion: String) : AppUpdateState
}
