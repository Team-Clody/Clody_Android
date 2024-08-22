package com.sopt.clody.presentation.ui.setting.notificationsetting.screen

import com.sopt.clody.data.remote.dto.response.ResponseSendNotificationDto

sealed class NotificationTimeChangeState {
    data object Idle: NotificationTimeChangeState()
    data object Loading: NotificationTimeChangeState()
    data class Success(val data: ResponseSendNotificationDto): NotificationTimeChangeState()
    data class Failure(val errorMessage: String): NotificationTimeChangeState()
}
