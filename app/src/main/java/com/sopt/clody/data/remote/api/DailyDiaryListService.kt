package com.sopt.clody.data.remote.api

import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.response.DailyDiariesResponseDto
import retrofit2.http.DELETE
import retrofit2.http.Query

interface DailyDiaryListService {
    @DELETE("api/v1/diary")
    suspend fun deleteDailyDiary(
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("date") date: Int
    ): ApiResponse<DailyDiariesResponseDto>
}
