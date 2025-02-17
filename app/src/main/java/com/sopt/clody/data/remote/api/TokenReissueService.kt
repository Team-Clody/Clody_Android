package com.sopt.clody.data.remote.api

import com.sopt.clody.data.remote.dto.response.TokenReissueResponseDto
import com.sopt.clody.data.remote.dto.base.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface TokenReissueService {
    @GET("api/v1/auth/reissue")
    suspend fun reissue(
        @Header("Authorization") refreshToken: String
    ): ApiResponse<TokenReissueResponseDto>
}
