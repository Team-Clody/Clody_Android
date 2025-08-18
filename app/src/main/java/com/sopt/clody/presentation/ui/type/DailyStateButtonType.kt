package com.sopt.clody.presentation.ui.type

import com.sopt.clody.domain.model.MonthlyCalendarInfo
import com.sopt.clody.domain.type.ReplyStatus

enum class DailyStateButtonType {
    DRAFT_ENABLED, REPLY_ENABLED, REPLY_DISABLED, DIARY_ENABLED, DIARY_DISABLED;

    companion object {
        fun getButtonType(
            info: MonthlyCalendarInfo.DailyDiaryInfo,
            enableWriteDiary: Boolean,
        ): DailyStateButtonType = with(info) {
            when {
                isDraft && replyStatus == ReplyStatus.HAS_DRAFT -> DRAFT_ENABLED
                (isDeleted && diaryCount > 0) || replyStatus == ReplyStatus.INVALID_DRAFT -> REPLY_DISABLED
                replyStatus in setOf(ReplyStatus.READY_READ, ReplyStatus.READY_NOT_READ) ||
                    (replyStatus == ReplyStatus.UNREADY && diaryCount > 0) -> REPLY_ENABLED
                enableWriteDiary -> DIARY_ENABLED
                else -> DIARY_DISABLED
            }
        }
    }
}
