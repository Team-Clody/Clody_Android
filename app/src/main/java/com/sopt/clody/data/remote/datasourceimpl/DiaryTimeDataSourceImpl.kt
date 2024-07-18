package com.sopt.clody.data.remote.datasourceimpl

import com.sopt.clody.data.remote.api.DiaryTimeService
import com.sopt.clody.data.remote.datasource.DiaryTimeDataSource
import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.response.ResponseDiaryTimeDto
import javax.inject.Inject


class DiaryTimeDataSourceImpl @Inject constructor(
    private val diaryTimeService: DiaryTimeService
) : DiaryTimeDataSource{
    override suspend fun getDiaryTime(year: Int, month: Int, date: Int): ApiResponse<ResponseDiaryTimeDto> {
        return diaryTimeService.getDiaryTime(year, month, date)
    }
}
