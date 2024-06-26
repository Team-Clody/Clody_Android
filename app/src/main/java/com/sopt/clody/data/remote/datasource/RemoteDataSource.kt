package com.sopt.clody.data.remote.datasource

import com.sopt.clody.utils.base.ApiResponse
import com.sopt.clody.data.remote.dto.ExampleResponseDto

// 원격 데이터 소스와 상호작용하는 인터페이스
interface RemoteDataSource {
    suspend fun getExampleData(): ApiResponse<List<ExampleResponseDto>>
}
