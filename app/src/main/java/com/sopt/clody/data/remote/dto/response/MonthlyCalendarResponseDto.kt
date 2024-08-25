package com.sopt.clody.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MonthlyCalendarResponseDto(
    @SerialName("totalCloverCount") val totalCloverCount: Int,
    @SerialName("diaries") val diaries: List<Diary>
) {
    @Serializable
    data class Diary(
        @SerialName("diaryCount") val diaryCount: Int,
        @SerialName("replyStatus") val replyStatus: String,
        @SerialName("isDeleted") val isDeleted: Boolean
    )
}
