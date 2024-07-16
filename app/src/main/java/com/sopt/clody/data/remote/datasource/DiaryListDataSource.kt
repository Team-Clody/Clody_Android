package com.sopt.clody.data.remote.datasource

import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.diarylist.MonthlyDiaryData

interface DiaryListDataSource {
    suspend fun getMonthlyDiary(
        year: Int,
        month: Int
    ) : ApiResponse<MonthlyDiaryData>

    suspend fun deleteDailyDiary(
        year: Int,
        month: Int,
        date: Int
    ) : ApiResponse<Unit>
}
