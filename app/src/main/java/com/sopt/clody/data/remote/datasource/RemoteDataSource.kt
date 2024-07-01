package com.sopt.clody.data.remote.datasource

import com.sopt.clody.data.remote.dto.ExampleRequestDto
import com.sopt.clody.data.remote.dto.ExampleResponseDto
import com.sopt.clody.data.remote.dto.base.ApiResponse

interface RemoteDataSource {
    suspend fun getExampleData(): ApiResponse<List<ExampleResponseDto>>
    suspend fun postExampleData(exampleRequestDto: ExampleRequestDto): ApiResponse<Unit>
}
