package com.sopt.clody.presentation.ui.setting.accountmanagement.screen

import com.sopt.clody.data.remote.dto.ResponseUserInfoDto

sealed class UserInfoState {
    data object Idle : UserInfoState()
    data object Loading : UserInfoState()
    data class Success(val data: ResponseUserInfoDto) : UserInfoState()
    data class Failure(val errorMessage: String) : UserInfoState()
}
