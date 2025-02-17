package com.sopt.clody.presentation.ui.setting.screen

import com.sopt.clody.data.remote.dto.response.UserInfoResponseDto

sealed class UserInfoState {
    data object Idle : UserInfoState()
    data object Loading : UserInfoState()
    data class Success(val data: UserInfoResponseDto) : UserInfoState()
    data class Failure(val errorMessage: String) : UserInfoState()
}
