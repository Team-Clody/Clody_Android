package com.sopt.clody.data.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SaveDraftDiaryRequestDto(
    @SerialName("date") val date: String,
    @SerialName("draftDiaries") val draftDiaries: List<String>,
)
