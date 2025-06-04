
package com.sopt.clody.data.remote.dto.response

import com.sopt.clody.domain.model.CreatedDraftDiaryInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DraftDiaryCreatedResponseDto(
    @SerialName("createdAt")
    val createdAt: String,
) {
    fun toDomain() = CreatedDraftDiaryInfo(
        createdAt = createdAt,
    )
}
