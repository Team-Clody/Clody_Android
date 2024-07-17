package com.sopt.clody.data.remote.api

import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.response.DailyDiariesResponseDto
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CalendarApiService {
    @GET("api/v1/calendar")
    suspend fun getMonthlyCalendarData(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): ApiResponse<MonthlyCalendarResponseDto>

    @GET("api/v1/diary")
    suspend fun getDailyDiariesData(
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("date") date: Int
    ): ApiResponse<DailyDiariesResponseDto>

}
