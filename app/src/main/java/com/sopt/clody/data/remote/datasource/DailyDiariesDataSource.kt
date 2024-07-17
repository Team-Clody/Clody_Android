package com.sopt.clody.data.remote.datasource

import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.response.DailyDiariesResponseDto

interface DailyDiariesDataSource {
    suspend fun getDailyDiariesData(year: Int, month: Int,date:Int): ApiResponse<DailyDiariesResponseDto>
}
