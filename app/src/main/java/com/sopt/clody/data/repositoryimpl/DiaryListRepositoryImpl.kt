package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.remote.datasource.DiaryListDataSource
import com.sopt.clody.data.remote.dto.diarylist.MonthlyDiaryData
import com.sopt.clody.data.repository.DiaryListRepository
import com.sopt.clody.presentation.utils.extension.handleApiResponse
import javax.inject.Inject

class DiaryListRepositoryImpl @Inject constructor(
    private val diaryListDataSource: DiaryListDataSource
) : DiaryListRepository {
    override suspend fun getMonthlyDiary(year: Int, month: Int): Result<MonthlyDiaryData> {
        return runCatching {
            diaryListDataSource.getMonthlyDiary(year, month).handleApiResponse().getOrThrow()
        }
    }

    override suspend fun deleteDailyDiary(year: Int, month: Int, date: Int): Result<Unit> {
        return runCatching {
            diaryListDataSource.deleteDailyDiary(year, month, date).handleApiResponse().getOrThrow()
        }
    }

}
