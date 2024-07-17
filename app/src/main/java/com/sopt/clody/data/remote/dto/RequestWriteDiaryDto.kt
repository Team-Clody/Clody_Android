package com.sopt.clody.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestWriteDiaryDto(
    @SerialName("date") val date: String,
    @SerialName("content")val content: List<String>
)
