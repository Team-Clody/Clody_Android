package com.sopt.clody.presentation.ui.setting.notificationsetting.screen

import com.sopt.clody.data.remote.dto.response.SendNotificationResponseDto

sealed class NotificationChangeState {
    data object Idle : NotificationChangeState()
    data object Loading : NotificationChangeState()
    data class Success(val data: SendNotificationResponseDto) : NotificationChangeState()
    data class Failure(val errorMessage: String) : NotificationChangeState()
}
