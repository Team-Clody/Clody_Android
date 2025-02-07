package com.sopt.clody.presentation.ui.setting.screen

import com.sopt.clody.data.remote.dto.response.ModifyNicknameResponseDto

sealed class UserNicknameState {
    data object Idle : UserNicknameState()
    data object Loading : UserNicknameState()
    data class Success(val data: ModifyNicknameResponseDto) : UserNicknameState()
    data class Failure(val errorMessage: String) : UserNicknameState()
}
