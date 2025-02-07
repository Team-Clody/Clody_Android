package com.sopt.clody.data.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequestDto(
    @SerialName("platform") val platform: String,
    @SerialName("name") val name: String,
    @SerialName("fcmToken") val fcmToken: String
)
