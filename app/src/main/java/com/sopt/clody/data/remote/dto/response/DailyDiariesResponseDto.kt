package com.sopt.clody.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyDiariesResponseDto(
    @SerialName("diaries") val diaries: List<Diary>
) {
    @Serializable
    data class Diary(
        @SerialName("content")val content: String
    )
}
