package com.sopt.clody.data.repository

import com.sopt.clody.data.remote.dto.diarylist.MonthlyDiaryData

interface DiaryListRepository {
    suspend fun getMonthlyDiary(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<MonthlyDiaryData>
    suspend fun deleteDailyDiary(
        accessToken: String,
        year: Int,
        month: Int,
        date: Int
    ) : Result<Unit>
}
