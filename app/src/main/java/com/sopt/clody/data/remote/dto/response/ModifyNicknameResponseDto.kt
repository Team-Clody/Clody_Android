package com.sopt.clody.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModifyNicknameResponseDto(
    @SerialName("name") val name: String
)
