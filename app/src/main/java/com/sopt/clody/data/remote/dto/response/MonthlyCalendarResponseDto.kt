package com.sopt.clody.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MonthlyCalendarResponseDto(
    @SerialName("status") val status: Int,
    @SerialName("message") val message: String,
    @SerialName("data") val data: Data
) {
    @Serializable
    data class Data(
        @SerialName("cloverCount") val monthlyCloverCount: Int,
        @SerialName("diaries") val diaries: List<Diary>
    ) {
        @Serializable
        data class Diary(
            @SerialName("diaryCount") val diaryCount: Int,
            @SerialName("replyStatus")val replyStatus: String
        )
    }
}
