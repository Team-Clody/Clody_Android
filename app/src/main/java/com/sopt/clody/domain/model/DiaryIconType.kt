package com.sopt.clody.domain.model

import com.sopt.clody.R
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto

enum class DiaryCloverType(val iconRes: Int) {
    TODAY_UNWRITTEN(R.drawable.ic_home_today_unwritten_clover),
    TODAY_WRITTEN(R.drawable.ic_home_today_written_clover),
    UNGIVEN_CLOVER(R.drawable.ic_home_ungiven_clover),
    BOTTOM_CLOVER(R.drawable.ic_home_bottom_clover),
    MID_CLOVER(R.drawable.ic_home_mid_clover),
    TOP_CLOVER(R.drawable.ic_home_top_clover);

    companion object {
        fun getCalendarCloverType(diaryData: MonthlyCalendarResponseDto.Diary, isToday: Boolean): DiaryCloverType {
            return when {
                isToday && diaryData.diaryCount == 0 -> TODAY_UNWRITTEN
                isToday && diaryData.diaryCount > 0 &&
                        (diaryData.replyStatus == "UNREADY" || diaryData.replyStatus == "READY_NOT_READ") -> TODAY_WRITTEN
                diaryData.replyStatus == "READY_NOT_READ" && diaryData.diaryCount > 0 -> UNGIVEN_CLOVER
                diaryData.replyStatus == "UNREADY" && diaryData.diaryCount > 0 -> UNGIVEN_CLOVER
                diaryData.diaryCount == 0 -> UNGIVEN_CLOVER
                diaryData.diaryCount in 1..2 -> BOTTOM_CLOVER
                diaryData.diaryCount in 3..4 -> MID_CLOVER
                diaryData.diaryCount == 5 -> TOP_CLOVER
                else -> UNGIVEN_CLOVER
            }
        }
    }
}
