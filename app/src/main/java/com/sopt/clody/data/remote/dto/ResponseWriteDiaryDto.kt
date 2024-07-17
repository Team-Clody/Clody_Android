package com.sopt.clody.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseWriteDiaryDto(
    @SerialName("createAt") val createdAt: String
)
