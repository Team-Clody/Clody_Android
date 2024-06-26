package com.sopt.clody.data.remote.datasource

import com.sopt.clody.utils.base.ApiResponse
import com.sopt.clody.data.remote.api.ApiService
import com.sopt.clody.data.remote.dto.ExampleResponseDto
import javax.inject.Inject

// RemoteDataSource 인터페이스 구현체
class RemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : RemoteDataSource {
    override suspend fun getExampleData(): ApiResponse<List<ExampleResponseDto>> {
        return apiService.getExampleData()
    }
}
