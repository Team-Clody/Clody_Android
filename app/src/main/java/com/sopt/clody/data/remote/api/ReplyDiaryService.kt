package com.sopt.clody.data.remote.api

import com.sopt.clody.data.remote.dto.response.ResponseReplyDiaryDto
import com.sopt.clody.data.remote.dto.base.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ReplyDiaryService {
    @GET("api/v1/reply")
    suspend fun getReplyDiary(
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("date") date: Int
    ): ApiResponse<ResponseReplyDiaryDto>
}
