package com.sopt.clody.data.remote.datasourceimpl

import com.sopt.clody.data.remote.api.DiaryListService
import com.sopt.clody.data.remote.datasource.DiaryListDataSource
import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.diarylist.MonthlyDiaryData
import javax.inject.Inject

class DiaryListDataSourceImpl @Inject constructor(
    private val diaryListService: DiaryListService
): DiaryListDataSource {
    override suspend fun getMonthlyDiary(year: Int, month: Int): ApiResponse<MonthlyDiaryData> {
        return diaryListService.getMonthlyDiary(year, month)
    }

    override suspend fun deleteDailyDiary(year: Int, month: Int, date: Int): ApiResponse<Unit> {
        return diaryListService.deleteDailyDiary(year, month, date)
    }
}
