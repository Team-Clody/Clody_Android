package com.sopt.clody.data.remote.datasourceimpl

import com.sopt.clody.data.remote.datasource.MonthlyCalendarDataSource
import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import javax.inject.Inject

class MonthlyCalendarDataSourceImpl @Inject constructor(
    private val calendarApi: CalendarApiService
) : MonthlyCalendarDataSource {
    override suspend fun getCalendarData(year: Int, month: Int): ApiResponse<MonthlyCalendarResponseDto> {
        return calendarApi.getMonthlyCalendarData(year, month)
    }
}
