package com.sopt.clody.data.remote.dto.diarylist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseMonthlyDiaryDto(
    @SerialName("totalMonthlyCount") val totalMonthlyCount: Int,
    @SerialName("diaries") val diaries: List<DailyDiary>
) {
    @Serializable
    data class DailyDiary(
        @SerialName("diaryCount") val diaryCount: Int,
        @SerialName("replyStatus") val replyStatus: String,
        @SerialName("date") val date: String,
        @SerialName("diary") val diary: List<DailyDiaryContent>
    ) {
        @Serializable
        data class DailyDiaryContent(
            @SerialName("content") val content: String
        )
    }
}
