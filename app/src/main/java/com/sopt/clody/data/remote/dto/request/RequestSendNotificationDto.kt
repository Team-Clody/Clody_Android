package com.sopt.clody.data.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestSendNotificationDto(
    @SerialName("isDiaryAlarm") val isDiaryAlarm: Boolean,
    @SerialName("isReplyAlarm") val isReplyAlarm: Boolean,
    @SerialName("time") val time: String,
    @SerialName("fcmToken") val fcmToken: String
)
