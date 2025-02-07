package com.sopt.clody.presentation.ui.setting.notificationsetting.screen

import com.sopt.clody.data.remote.dto.response.NotificationInfoResponseDto

sealed class NotificationInfoState {
    data object Idle : NotificationInfoState()
    data object Loading : NotificationInfoState()
    data class Success(val data: NotificationInfoResponseDto) : NotificationInfoState()
    data class Failure(val errorMessage: String) : NotificationInfoState()
}
