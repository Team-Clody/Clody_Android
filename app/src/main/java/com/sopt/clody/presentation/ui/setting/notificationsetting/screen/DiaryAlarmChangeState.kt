package com.sopt.clody.presentation.ui.setting.notificationsetting.screen

import com.sopt.clody.data.remote.dto.response.SendNotificationResponseDto

sealed class DiaryAlarmChangeState {
    data object Idle : DiaryAlarmChangeState()
    data object Loading : DiaryAlarmChangeState()
    data class Success(val data: SendNotificationResponseDto) : DiaryAlarmChangeState()
    data class Failure(val errorMessage: String) : DiaryAlarmChangeState()
}
