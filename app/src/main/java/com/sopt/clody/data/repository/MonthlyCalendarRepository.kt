package com.sopt.clody.data.repository

import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto

interface MonthlyCalendarRepository {
    suspend fun getMonthlyCalendarData(year: Int, month: Int): Result<MonthlyCalendarResponseDto>
}
