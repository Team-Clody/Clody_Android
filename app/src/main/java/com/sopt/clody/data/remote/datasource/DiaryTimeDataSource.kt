package com.sopt.clody.data.remote.datasource

import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.response.ResponseDiaryTimeDto

interface DiaryTimeDataSource {
    suspend fun getDiaryTime(year: Int, month: Int, date: Int): ApiResponse<ResponseDiaryTimeDto>
}
