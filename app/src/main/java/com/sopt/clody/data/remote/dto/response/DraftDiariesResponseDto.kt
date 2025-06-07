package com.sopt.clody.data.remote.dto.response

import com.sopt.clody.domain.model.DraftDiaryContents
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DraftDiariesResponseDto(
    @SerialName("draftDiaries") val draftDiaries: List<String>,
) {
    fun toDomain() = DraftDiaryContents(
        draftDiaries = draftDiaries ?: emptyList(),
    )
}
