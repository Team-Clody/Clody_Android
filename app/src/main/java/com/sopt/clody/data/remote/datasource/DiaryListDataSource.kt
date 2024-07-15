package com.sopt.clody.data.remote.datasource

import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.diarylist.MonthlyDiaryData

interface DiaryListDataSource {
    suspend fun getMonthlyDiary(
        accessToken: String,
        year: Int,
        month: Int
    ) : ApiResponse<MonthlyDiaryData>

    suspend fun deleteDailyDiary(
        accessToken: String,
        year: Int,
        month: Int,
        date: Int
    ) : ApiResponse<Unit>
}
