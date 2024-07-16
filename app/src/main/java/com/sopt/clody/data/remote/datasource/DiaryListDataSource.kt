package com.sopt.clody.data.remote.datasource

import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.diarylist.ResponseMonthlyDiaryDto

interface DiaryListDataSource {
    suspend fun getMonthlyDiary(
        year: Int,
        month: Int
    ): ApiResponse<ResponseMonthlyDiaryDto>
}
