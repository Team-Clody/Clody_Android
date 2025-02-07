package com.sopt.clody.data.remote.api

import com.sopt.clody.data.remote.dto.request.WriteDiaryRequestDto
import com.sopt.clody.data.remote.dto.response.WriteDiaryResponseDto
import com.sopt.clody.data.remote.dto.base.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface WriteDiaryService {
    @POST("api/v1/diary")
    suspend fun writeDiary(
        @Body writeDiaryRequestDto: WriteDiaryRequestDto
    ): ApiResponse<WriteDiaryResponseDto>
}
