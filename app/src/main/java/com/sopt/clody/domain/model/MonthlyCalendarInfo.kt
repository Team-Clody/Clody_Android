package com.sopt.clody.domain.model

import com.sopt.clody.domain.type.ReplyStatus
import java.time.LocalDate

data class MonthlyCalendarInfo(
    val year: Int = LocalDate.now().year,
    val month: Int = LocalDate.now().monthValue,
    val totalCloverCount: Int = 0,
    val dailyDiaryInfoList: List<DailyDiaryInfo> = listOf()
) {
    data class DailyDiaryInfo(
        val diaryCount: Int = 0,
        val replyStatus: ReplyStatus = ReplyStatus.UNREADY,
        val date: String = "",
        val diaryList: List<String> = listOf(),
        val isDeleted: Boolean = false,
        val isDraft: Boolean = false
    )
}
