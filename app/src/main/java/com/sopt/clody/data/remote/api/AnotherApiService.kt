package com.sopt.clody.data.remote.api

import com.sopt.clody.data.remote.dto.ExampleResponseDto
import retrofit2.http.GET

interface AnotherApiService {
    @GET("api/v1/data")
    suspend fun getAnotherData(): List<ExampleResponseDto>
}
