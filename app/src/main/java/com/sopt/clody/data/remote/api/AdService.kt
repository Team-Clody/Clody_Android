package com.sopt.clody.data.remote.api

import com.sopt.clody.data.remote.dto.base.NullableApiResponse
import com.sopt.clody.data.remote.dto.request.AdRequestDto
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AdService {
    @POST("api/v1/reply/ad/start")
    suspend fun startAd(
        @Body adRequestDto: AdRequestDto
    ) : NullableApiResponse<Unit>

    @PATCH("api/v1/reply/ad/end")
    suspend fun endAd(
        @Body adRequestDto: AdRequestDto
    ) : NullableApiResponse<Unit>
}
