package com.sopt.clody.data.remote.api

import com.sopt.clody.data.remote.dto.ExampleRequestDto
import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.ExampleResponseDto
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("api/v1/data")
    suspend fun getExampleData(): ApiResponse<List<ExampleResponseDto>>
    @POST("api/v1/data")
    suspend fun postExampleData(
        @Body exampleRequestDto: ExampleRequestDto
    ): ApiResponse<Unit>

    @GET("api/v1/calendar")
    suspend fun getMonthlyCalendarData(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): ApiResponse<List<MonthlyCalendarResponseDto>>
}
