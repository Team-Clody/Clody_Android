package com.sopt.clody.data.remote.dto.response

import com.sopt.clody.domain.type.ReplyStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MonthlyDiaryResponseDto(
    @SerialName("totalCloverCount") val totalCloverCount: Int,
    @SerialName("diaries") val diaries: List<DailyDiary>,
) {
    @Serializable
    data class DailyDiary(
        @SerialName("diaryCount") val diaryCount: Int,
        @SerialName("replyStatus") val replyStatus: ReplyStatus,
        @SerialName("date") val date: String,
        @SerialName("diary") val diary: List<DailyDiaryContent>,
        @SerialName("isDeleted") val isDeleted: Boolean,
    ) {
        @Serializable
        data class DailyDiaryContent(
            @SerialName("content") val content: String,
        )
    }
}
