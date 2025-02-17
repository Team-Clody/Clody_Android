package com.sopt.clody.presentation.ui.setting.notificationsetting.screen

import com.sopt.clody.data.remote.dto.response.SendNotificationResponseDto

sealed class NotificationTimeChangeState {
    data object Idle : NotificationTimeChangeState()
    data object Loading : NotificationTimeChangeState()
    data class Success(val data: SendNotificationResponseDto) : NotificationTimeChangeState()
    data class Failure(val errorMessage: String) : NotificationTimeChangeState()
}
