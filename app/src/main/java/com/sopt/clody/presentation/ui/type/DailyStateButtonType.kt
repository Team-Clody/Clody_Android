package com.sopt.clody.presentation.ui.type

import com.sopt.clody.domain.model.CalendarMonthlyInfo
import com.sopt.clody.domain.model.DailyDiaryInfo
import com.sopt.clody.domain.type.ReplyStatus

enum class DailyStateButtonType {
    DRAFT_ENABLED, REPLY_ENABLED, REPLY_DISABLED, DIARY_ENABLED, DIARY_DISABLED;

    companion object {
        fun getType(
            calendarDailyInfo: CalendarMonthlyInfo.CalendarDailyInfo,
            dailyDiaryInfo: DailyDiaryInfo,
        ): DailyStateButtonType {
            return when {
                calendarDailyInfo.isDeleted && calendarDailyInfo.diaryCount > 0 -> REPLY_DISABLED
                dailyDiaryInfo.isDraft -> DRAFT_ENABLED
                calendarDailyInfo.replyStatus == ReplyStatus.INVALID_DRAFT -> REPLY_DISABLED
                calendarDailyInfo.replyStatus == ReplyStatus.READY_READ ||
                    calendarDailyInfo.replyStatus == ReplyStatus.READY_NOT_READ ||
                    (calendarDailyInfo.replyStatus == ReplyStatus.UNREADY && calendarDailyInfo.diaryCount > 0) -> REPLY_ENABLED
                calendarDailyInfo.enableWriteDiary() -> DIARY_ENABLED
                else -> DIARY_DISABLED
            }
        }
    }
}
