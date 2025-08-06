package com.sopt.clody.presentation.ui.type

import androidx.annotation.DrawableRes
import com.sopt.clody.R
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import com.sopt.clody.domain.model.ReplyStatus

/**
 * DiaryData를 기반으로 해당 날짜에 보여줄 클로버 아이콘 타입을 반환.
 *
 * - 오늘이고 일기가 없으면 👉 [TODAY_UNWRITTEN]
 * - 오늘이고 일기와 읽지 않은 답장이 있으면 👉 [TODAY_WRITTEN]
 * - 임시저장이 존재하면 👉 [DRAFT_SAVED]
 * - 임시저장이 만료되었으면 👉 [EXPIRED_WRITTEN]
 * - 일기가 있고 답장이 없거나 읽지 않았으면 👉 [UNGIVEN_CLOVER]
 * - 일기 수에 따라 👉 [BOTTOM_CLOVER], [MID_CLOVER], [TOP_CLOVER] 구분
 * - 이 외의 경우 기본값 👉 [UNGIVEN_CLOVER]
 */

enum class DiaryCloverType(@DrawableRes val iconRes: Int) {
    TODAY_UNWRITTEN(R.drawable.ic_home_today_unwritten_clover),
    TODAY_WRITTEN(R.drawable.ic_home_today_written_clover),
    UNGIVEN_CLOVER(R.drawable.ic_home_ungiven_clover),
    BOTTOM_CLOVER(R.drawable.ic_home_bottom_clover),
    MID_CLOVER(R.drawable.ic_home_mid_clover),
    TOP_CLOVER(R.drawable.ic_home_top_clover),
    DRAFT_SAVED(R.drawable.ic_home_draft_saved_clover),
    EXPIRED_WRITTEN(R.drawable.ic_home_expired_written_clover),
    ;

    companion object {
        fun getCalendarCloverType(
            diaryData: MonthlyCalendarResponseDto.Diary,
            isToday: Boolean,
        ): DiaryCloverType {
            val count = diaryData.diaryCount
            val reply = diaryData.replyStatus

            val hasDiary = count > 0
            val noDiary = count == 0
            val hasDraft = reply == ReplyStatus.HAS_DRAFT
            val draftExpired = reply == ReplyStatus.INVALID_DRAFT
            val hasUnreadOrNoReply = reply.isUnreadOrNotRead

            return when {
                hasDraft -> DRAFT_SAVED
                isToday && noDiary -> TODAY_UNWRITTEN
                isToday && hasUnreadOrNoReply -> TODAY_WRITTEN
                draftExpired -> EXPIRED_WRITTEN
                hasDiary && hasUnreadOrNoReply -> UNGIVEN_CLOVER
                count in 1..2 -> BOTTOM_CLOVER
                count in 3..4 -> MID_CLOVER
                count >= 5 -> TOP_CLOVER
                else -> UNGIVEN_CLOVER
            }
        }
    }
}
