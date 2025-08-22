package com.sopt.clody.presentation.ui.type

import androidx.annotation.DrawableRes
import com.sopt.clody.R
import com.sopt.clody.domain.model.CalendarMonthlyInfo
import com.sopt.clody.domain.type.ReplyStatus

/**
 * DiaryData를 기반으로 해당 날짜에 보여줄 클로버 아이콘 타입을 반환.
 *
 * - 오늘이고 일기가 없으면 👉 [ENABLED_DIARY]
 * - 오늘이고 일기와 읽지 않은 답장이 있으면 👉 [WAITING_REPLY]
 * - 임시저장이 존재하면 👉 [DRAFT_SAVED]
 * - 임시저장이 만료되었으면 👉 [DISABLED_REPLY]
 * - 일기가 있고 답장이 없거나 읽지 않았으면 👉 [UNGIVEN_CLOVER]
 * - 일기 수에 따라 👉 [BOTTOM_CLOVER], [MID_CLOVER], [TOP_CLOVER] 구분
 * - 이 외의 경우 기본값 👉 [UNGIVEN_CLOVER]
 */

enum class DailyCloverType(@DrawableRes val iconRes: Int) {
    DRAFT_SAVED(R.drawable.ic_home_draft_saved_clover),
    DISABLED_REPLY(R.drawable.ic_home_disabled_reply_clover),
    ENABLED_DIARY(R.drawable.ic_home_enabled_diary_clover),
    WAITING_REPLY(R.drawable.ic_home_waiting_reply_clover),
    UNGIVEN_CLOVER(R.drawable.ic_home_ungiven_clover),
    BOTTOM_CLOVER(R.drawable.ic_home_bottom_clover),
    MID_CLOVER(R.drawable.ic_home_mid_clover),
    TOP_CLOVER(R.drawable.ic_home_top_clover),
    ;

    companion object {
        fun getType(info: CalendarMonthlyInfo.CalendarDailyInfo): DailyCloverType {
            return when {
                info.replyStatus == ReplyStatus.HAS_DRAFT -> DRAFT_SAVED
                info.replyStatus == ReplyStatus.INVALID_DRAFT || (info.isDeleted && info.diaryCount > 0) -> DISABLED_REPLY
                info.isToday() && info.diaryCount == 0 -> ENABLED_DIARY
                info.isToday() && info.replyStatus == ReplyStatus.UNREADY && info.diaryCount > 0 -> WAITING_REPLY
                info.replyStatus == ReplyStatus.READY_READ && info.diaryCount in 1..2 -> BOTTOM_CLOVER
                info.replyStatus == ReplyStatus.READY_READ && info.diaryCount in 3..4 -> MID_CLOVER
                info.replyStatus == ReplyStatus.READY_READ && info.diaryCount >= 5 -> TOP_CLOVER
                else -> UNGIVEN_CLOVER
            }
        }
    }
}
