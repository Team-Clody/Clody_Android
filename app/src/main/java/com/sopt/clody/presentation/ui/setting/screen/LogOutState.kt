package com.sopt.clody.presentation.ui.setting.screen

sealed class LogOutState {
    object Idle : LogOutState()
    object Success : LogOutState()
    data class Failure(val error: String) : LogOutState()
}
