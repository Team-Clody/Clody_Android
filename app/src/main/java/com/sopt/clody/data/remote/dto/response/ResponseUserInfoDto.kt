package com.sopt.clody.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseUserInfoDto(
    @SerialName("email") val email: String,
    @SerialName("name") val name: String,
    @SerialName("platform") val platform: String
)
