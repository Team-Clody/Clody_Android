package com.sopt.clody.data.remote.datasourceimpl

import com.sopt.clody.data.remote.api.ApiService
import com.sopt.clody.data.remote.datasource.RemoteDataSource
import com.sopt.clody.data.remote.dto.ExampleRequestDto
import com.sopt.clody.data.remote.dto.ExampleResponseDto
import com.sopt.clody.data.remote.dto.base.ApiResponse
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : RemoteDataSource {
    override suspend fun getExampleData(): ApiResponse<List<ExampleResponseDto>> {
        return apiService.getExampleData()
    }
    override suspend fun postExampleData(exampleRequestDto: ExampleRequestDto): ApiResponse<Unit> {
        return apiService.postExampleData(exampleRequestDto)
    }
}
