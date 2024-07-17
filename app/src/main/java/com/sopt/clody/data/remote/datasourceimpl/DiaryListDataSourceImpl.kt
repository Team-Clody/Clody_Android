package com.sopt.clody.data.remote.datasourceimpl

import com.sopt.clody.data.remote.api.DiaryListService
import com.sopt.clody.data.remote.datasource.DiaryListDataSource
import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.diarylist.ResponseMonthlyDiaryDto
import javax.inject.Inject

class DiaryListDataSourceImpl @Inject constructor(
    private val diaryListService: DiaryListService
): DiaryListDataSource {
    override suspend fun getMonthlyDiary(year: Int, month: Int): ApiResponse<ResponseMonthlyDiaryDto> {
        return diaryListService.getMonthlyDiary(year, month)
    }
}
