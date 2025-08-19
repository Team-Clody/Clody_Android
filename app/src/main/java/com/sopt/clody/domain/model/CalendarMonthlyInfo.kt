package com.sopt.clody.domain.model

import com.sopt.clody.domain.type.ReplyStatus
import java.time.LocalDate
import java.time.ZoneId

data class CalendarMonthlyInfo(
    val totalCloverCount: Int = 0,
    val calendarDailyInfoList: List<CalendarDailyInfo> = listOf(),
) {
    data class CalendarDailyInfo(
        val diaryCount: Int = 0,
        val replyStatus: ReplyStatus = ReplyStatus.UNREADY,
        val date: String = "",
        val isDeleted: Boolean = false,
    ) {
        fun enableWriteDiary(): Boolean {
            val userTimeZone = ZoneId.systemDefault().id
            val today = LocalDate.now().toString()
            val yesterday = LocalDate.now().minusDays(1).toString()
            val isAvailableDay = if (userTimeZone == "Asia/Seoul") {
                date == today || date == yesterday
            } else {
                date == today
            }
            return diaryCount == 0 && isAvailableDay
        }
    }
}
