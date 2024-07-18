package com.sopt.clody.data.remote.datasourceimpl

import com.sopt.clody.data.remote.api.DailyDiaryListService
import com.sopt.clody.data.remote.datasource.DailyDiaryListDataSource
import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.response.DailyDiariesResponseDto
import javax.inject.Inject

class DailyDiaryListDataSourceImpl@Inject constructor(
    private val service: DailyDiaryListService
) :DailyDiaryListDataSource {
    override suspend fun deleteDailyDiary(year: Int, month: Int, date: Int): ApiResponse<DailyDiariesResponseDto> {
        return service.deleteDailyDiary(year, month, date)
    }
}
