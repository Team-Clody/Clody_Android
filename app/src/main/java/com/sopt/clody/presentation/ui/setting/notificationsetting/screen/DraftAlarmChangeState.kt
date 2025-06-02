package com.sopt.clody.presentation.ui.setting.notificationsetting.screen

import com.sopt.clody.data.remote.dto.response.SendNotificationResponseDto

sealed class DraftAlarmChangeState {
    data object Idle : DraftAlarmChangeState()
    data object Loading : DraftAlarmChangeState()
    data class Success(val data: SendNotificationResponseDto) : DraftAlarmChangeState()
    data class Failure(val errorMessage: String) : DraftAlarmChangeState()
}
