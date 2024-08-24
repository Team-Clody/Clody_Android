package com.sopt.clody.presentation.ui.setting.notificationsetting.screen

import com.sopt.clody.data.remote.dto.response.ResponseSendNotificationDto

sealed class DiaryAlarmChangeState {
    data object Idle : DiaryAlarmChangeState()
    data object Loading : DiaryAlarmChangeState()
    data class Success(val data: ResponseSendNotificationDto) : DiaryAlarmChangeState()
    data class Failure(val errorMessage: String) : DiaryAlarmChangeState()
}
