package com.sopt.clody.data.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
   @SerialName("platform") val platform: String,
   @SerialName("fcmToken") val fcmToken: String
)
