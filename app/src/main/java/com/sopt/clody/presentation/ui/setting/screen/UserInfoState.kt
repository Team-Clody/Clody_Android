package com.sopt.clody.presentation.ui.setting.screen

import com.sopt.clody.data.remote.dto.ResponseUserInfoDto
import com.sopt.clody.data.remote.dto.diarylist.ResponseMonthlyDiaryDto
import com.sopt.clody.presentation.ui.diarylist.screen.DiaryListState

sealed class UserInfoState {
    data object Idle : UserInfoState()
    data object Loading : UserInfoState()
    data class Success(val data: ResponseUserInfoDto) : UserInfoState()
    data class Failure(val errorMessage: String) : UserInfoState()
}
