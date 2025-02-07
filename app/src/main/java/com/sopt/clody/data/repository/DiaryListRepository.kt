package com.sopt.clody.data.repository

import com.sopt.clody.data.remote.dto.response.MonthlyDiaryResponseDto

interface DiaryListRepository {
    suspend fun getMonthlyDiary(
        year: Int,
        month: Int
    ): Result<MonthlyDiaryResponseDto>
}
