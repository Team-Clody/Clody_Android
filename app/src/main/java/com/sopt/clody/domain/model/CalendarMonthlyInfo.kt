package com.sopt.clody.domain.model

import com.sopt.clody.domain.type.ReplyStatus
import java.time.LocalDate
import java.time.ZoneId

/**
 * 홈 화면의 월별 달력에서 사용되는 정보
 *
 * @property totalCloverCount 지금까지 모은 클로버(답장)의 개수. 연 단위로 카운트
 * @property calendarDailyInfoList 해당 월의 일별 정보
 *
 * */
data class CalendarMonthlyInfo(
    val totalCloverCount: Int = 0,
    val calendarDailyInfoList: List<CalendarDailyInfo> = listOf(),
) {
    /**
     *  홈 화면 월별 달력을 구성하는 일별 일기 정보
     *
     * @property diaryCount 해당 일에 작성한 일기의 개수
     * @property replyStatus 해당 일에 작성한 일기의 답장 상태
     * @property date 해당 일의 날짜, "2025-08-21" 형식
     * @property isDeleted 해당 일에 작성한 일기가 삭제 이력의 여부
     *
     * */
    data class CalendarDailyInfo(
        val diaryCount: Int = 0,
        val replyStatus: ReplyStatus = ReplyStatus.UNREADY,
        val date: String = "",
        val isDeleted: Boolean = false,
    ) {
        fun isToday(): Boolean = date == LocalDate.now().toString()

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
