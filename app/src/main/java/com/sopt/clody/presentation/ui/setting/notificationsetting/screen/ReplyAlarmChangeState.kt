package com.sopt.clody.presentation.ui.setting.notificationsetting.screen

import com.sopt.clody.data.remote.dto.response.ResponseSendNotificationDto

sealed class ReplyAlarmChangeState {
    data object Idle: ReplyAlarmChangeState()
    data object Loading: ReplyAlarmChangeState()
    data class Success(val data: ResponseSendNotificationDto): ReplyAlarmChangeState()
    data class Failure(val errorMessage: String): ReplyAlarmChangeState()
}
