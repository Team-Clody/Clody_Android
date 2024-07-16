package com.sopt.clody.data.remote.datasourceimpl

import com.sopt.clody.data.remote.api.CalendarApiService
import com.sopt.clody.data.remote.datasource.MonthlyCalendarDataSource
import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import javax.inject.Inject

class MonthlyCalendarDataSourceImpl @Inject constructor(
    private val api: CalendarApiService
) : MonthlyCalendarDataSource {
    override suspend fun getCalendarData(year: Int, month: Int): ApiResponse<MonthlyCalendarResponseDto> {
        return api.getMonthlyCalendarData(year, month)
    }
}
