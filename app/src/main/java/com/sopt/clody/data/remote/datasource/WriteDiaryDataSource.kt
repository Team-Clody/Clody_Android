package com.sopt.clody.data.remote.datasource

import com.sopt.clody.data.remote.dto.ResponseWriteDiaryDto
import com.sopt.clody.data.remote.dto.base.ApiResponse

interface WriteDiaryDataSource {
    suspend fun writeDiary(date: String, content: List<String>): ApiResponse<ResponseWriteDiaryDto>
}
