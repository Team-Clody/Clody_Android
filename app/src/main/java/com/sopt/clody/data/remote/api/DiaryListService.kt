package com.sopt.clody.data.remote.api

import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.diarylist.MonthlyDiaryData
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface DiaryListService {
    @GET("api/v1/calender/list")
    suspend fun getMonthlyDiary(
        @Query("year") year: Int,
        @Query("month") month: Int,
    ): ApiResponse<MonthlyDiaryData>

    @DELETE("api/v1/calender")
    suspend fun deleteDailyDiary(
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("date") date: Int,
    ): ApiResponse<Unit>
}
