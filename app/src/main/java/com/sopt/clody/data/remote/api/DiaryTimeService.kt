package com.sopt.clody.data.remote.api

import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.response.DiaryTimeResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface DiaryTimeService {
    @GET("api/v1/diary/time")
    suspend fun getDiaryTime(
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("date") date: Int
        ): ApiResponse<DiaryTimeResponseDto>
}
