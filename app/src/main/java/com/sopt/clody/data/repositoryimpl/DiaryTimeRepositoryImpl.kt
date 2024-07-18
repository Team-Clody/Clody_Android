package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.remote.datasource.DiaryTimeDataSource
import com.sopt.clody.data.remote.dto.response.ResponseDiaryTimeDto
import com.sopt.clody.data.repository.DiaryTimeRepository
import javax.inject.Inject

class DiaryTimeRepositoryImpl @Inject constructor(
    private val diaryTimeDataSource: DiaryTimeDataSource
) : DiaryTimeRepository {
    override suspend fun getDiaryTime(year: Int, month: Int, date: Int): Result<ResponseDiaryTimeDto> {
        return runCatching {
            diaryTimeDataSource.getDiaryTime(year, month, date).data
        }
    }
}

