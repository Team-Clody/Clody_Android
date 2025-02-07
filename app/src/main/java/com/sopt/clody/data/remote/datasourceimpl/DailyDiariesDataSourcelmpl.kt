package com.sopt.clody.data.remote.datasourceimpl

import com.sopt.clody.data.remote.datasource.DailyDiariesDataSource
import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.response.DailyDiariesResponseDto
import javax.inject.Inject

class DailyDiariesDataSourceImpl @Inject constructor(
    private val calendarApi: CalendarApiService
) : DailyDiariesDataSource {
    override suspend fun getDailyDiariesData(year: Int, month: Int, date: Int): ApiResponse<DailyDiariesResponseDto> {
        return calendarApi.getDailyDiariesData(year, month, date)
    }
}

