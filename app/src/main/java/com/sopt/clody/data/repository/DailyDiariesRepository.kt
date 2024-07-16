package com.sopt.clody.data.repository

import com.sopt.clody.data.remote.dto.response.DailyDiariesResponseDto

interface DailyDiariesRepository {
    suspend fun getDailyDiariesData(year: Int, month: Int,date:Int): Result<DailyDiariesResponseDto>
}
