package com.sopt.clody.data.remote.datasourceimpl

import com.sopt.clody.data.remote.datasource.DiaryListDataSource
import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.response.MonthlyDiaryResponseDto
import javax.inject.Inject

class DiaryListDataSourceImpl @Inject constructor(
    private val diaryListService: DiaryListService
): DiaryListDataSource {
    override suspend fun getMonthlyDiary(year: Int, month: Int): ApiResponse<MonthlyDiaryResponseDto> {
        return diaryListService.getMonthlyDiary(year, month)
    }
}
