package com.sopt.clody.data.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// 로그인
@Serializable
data class LoginRequestDto(
   @SerialName("platform") val platform: String
)
