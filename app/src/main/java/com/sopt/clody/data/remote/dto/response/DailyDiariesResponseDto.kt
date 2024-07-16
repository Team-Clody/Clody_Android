package com.sopt.clody.data.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class DailyDiariesResponseDto(
    val diaries: List<Diary>
) {
    @Serializable
    data class Diary(
        val content: String
    )
}
