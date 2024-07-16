package com.sopt.clody.data.repository

import com.sopt.clody.data.remote.dto.diarylist.MonthlyDiaryData

interface DiaryListRepository {
    suspend fun getMonthlyDiary(
        year: Int,
        month: Int
    ): Result<MonthlyDiaryData>
    suspend fun deleteDailyDiary(
        year: Int,
        month: Int,
        date: Int
    ) : Result<Unit>
}
