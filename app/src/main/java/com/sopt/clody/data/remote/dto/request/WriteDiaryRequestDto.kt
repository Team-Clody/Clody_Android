package com.sopt.clody.data.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WriteDiaryRequestDto(
    @SerialName("date") val date: String,
    @SerialName("content")val content: List<String>
)
