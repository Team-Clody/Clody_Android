package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.remote.datasource.DiaryListDataSource
import com.sopt.clody.data.remote.dto.response.MonthlyDiaryResponseDto
import com.sopt.clody.data.repository.DiaryListRepository
import com.sopt.clody.data.remote.util.handleApiResponse
import javax.inject.Inject

class DiaryListRepositoryImpl @Inject constructor(
    private val diaryListDataSource: DiaryListDataSource
) : DiaryListRepository {
    override suspend fun getMonthlyDiary(year: Int, month: Int): Result<MonthlyDiaryResponseDto> {
        return runCatching {
            diaryListDataSource.getMonthlyDiary(year, month).handleApiResponse().getOrThrow()
        }
    }
}
