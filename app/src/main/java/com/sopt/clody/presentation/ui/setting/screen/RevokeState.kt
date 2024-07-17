package com.sopt.clody.presentation.ui.setting.screen

sealed class RevokeState {
    data object Idle : RevokeState()
    data object Loading : RevokeState()
    data class Success(val data: Unit) : RevokeState()
    data class Failure(val errorMessage: String) : RevokeState()
}
