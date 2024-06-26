package com.sopt.clody.data.remote.api

import com.sopt.clody.utils.base.ApiResponse
import com.sopt.clody.data.remote.dto.ExampleResponseDto
import retrofit2.http.GET

interface ApiService {
    @GET("api/v1/data")
    suspend fun getExampleData(): ApiResponse<List<ExampleResponseDto>>
}
