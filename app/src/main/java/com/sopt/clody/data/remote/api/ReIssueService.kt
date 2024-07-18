package com.sopt.clody.data.remote.api

import com.sopt.clody.data.remote.dto.response.ReissueTokenResponseDto
import com.sopt.clody.data.remote.dto.base.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface ReIssueService {
    @GET("api/v1/auth/reissue")
    suspend fun reissue(
        @Header("Authorization") refreshToken: String
    ): ApiResponse<ReissueTokenResponseDto>
}
