package com.sopt.clody.data.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoogleSignUpRequestDto(
    @SerialName("idToken") val idToken: String,
    @SerialName("fcmToken") val fcmToken: String,
)
