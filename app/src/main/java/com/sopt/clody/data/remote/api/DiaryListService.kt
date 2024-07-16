package com.sopt.clody.data.remote.api

import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.diarylist.ResponseMonthlyDiaryDto
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Query

interface DiaryListService {
    @GET("api/v1/calendar/list")
    suspend fun getMonthlyDiary(
        @Query("year") year: Int,
        @Query("month") month: Int,
    ): ApiResponse<ResponseMonthlyDiaryDto>

    @DELETE("api/v1/calendar")
    suspend fun deleteDailyDiary(
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("date") date: Int,
    ): ApiResponse<Unit>
}
