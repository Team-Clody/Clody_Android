package com.sopt.clody.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationInfoResponseDto(
    @SerialName("isDiaryAlarm") val isDiaryAlarm: Boolean,
    @SerialName("isReplyAlarm") val isReplyAlarm: Boolean,
    @SerialName("time") val time: String
)
