package com.sopt.clody.presentation.ui.setting.screen

sealed class RevokeAccountState {
    data object Idle : RevokeAccountState()
    data object Loading : RevokeAccountState()
    data class Success(val data: Unit) : RevokeAccountState()
    data class Failure(val errorMessage: String) : RevokeAccountState()
}
