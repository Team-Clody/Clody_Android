package com.sopt.clody.data.remote.api

import com.sopt.clody.data.remote.dto.ExampleRequestDto
import com.sopt.clody.data.remote.dto.ExampleResponseDto
import com.sopt.clody.data.remote.dto.base.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("api/v1/data")
    suspend fun getExampleData(): ApiResponse<List<ExampleResponseDto>>

    @POST("api/v1/data")
    suspend fun postExampleData(
        @Body exampleRequestDto: ExampleRequestDto
    ): ApiResponse<Unit>
}
