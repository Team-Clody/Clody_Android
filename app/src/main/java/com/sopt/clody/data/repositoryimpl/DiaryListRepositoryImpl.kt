package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.remote.datasource.DiaryListDataSource
import com.sopt.clody.data.remote.dto.diarylist.MonthlyDiaryData
import com.sopt.clody.data.repository.DiaryListRepository
import com.sopt.clody.presentation.utils.extension.handleApiResponse
import javax.inject.Inject

class DiaryListRepositoryImpl @Inject constructor(
    private val diaryListDataSource: DiaryListDataSource
) : DiaryListRepository {
    override suspend fun getMonthlyDiary(accessToken: String, year: Int, month: Int): Result<MonthlyDiaryData> {
        return runCatching {
            diaryListDataSource.getMonthlyDiary(accessToken, year, month).handleApiResponse().getOrThrow()
        }
    }

    override suspend fun deleteDailyDiary(accessToken: String, year: Int, month: Int, date: Int): Result<Unit> {
        return runCatching {
            diaryListDataSource.deleteDailyDiary(accessToken, year, month, date).handleApiResponse().getOrThrow()
        }
    }

}
