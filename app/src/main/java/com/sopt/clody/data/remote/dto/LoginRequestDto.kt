package com.sopt.clody.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// 로그인
@Serializable
data class LoginRequestDto(
   @SerialName("platform") val platform: String
)
