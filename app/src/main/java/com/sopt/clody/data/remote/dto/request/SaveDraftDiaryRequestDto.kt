package com.sopt.clody.data.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SaveDraftDiaryRequestDto(
    @SerialName("draftDiaries") val draftDiaries: List<String>,
)
