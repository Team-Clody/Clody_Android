package com.sopt.clody.data.remote.datasource

import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto

interface MonthlyCalendarDataSource {
    suspend fun getCalendarData(year: Int, month: Int): ApiResponse<List<MonthlyCalendarResponseDto>>
}
