package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.remote.api.DailyDiaryListService
import com.sopt.clody.data.remote.datasource.DailyDiaryListDataSource
import com.sopt.clody.data.remote.dto.response.DailyDiariesResponseDto
import com.sopt.clody.data.repository.DailyDiaryListRepository
import com.sopt.clody.presentation.utils.extension.handleApiResponse
import javax.inject.Inject

class DailyDiaryListRepositoryImpl @Inject constructor(
    private val service: DailyDiaryListService
) : DailyDiaryListRepository {
    override suspend fun deleteDailyDiary(year: Int, month: Int, day: Int): Result<DailyDiariesResponseDto> {
        return try {
            val response = service.deleteDailyDiary(year, month, day)
            response.handleApiResponse()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
