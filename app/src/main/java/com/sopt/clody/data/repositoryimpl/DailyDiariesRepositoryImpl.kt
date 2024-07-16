package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.remote.datasource.DailyDiariesDataSource
import com.sopt.clody.data.remote.dto.response.DailyDiariesResponseDto
import com.sopt.clody.data.repository.DailyDiariesRepository
import com.sopt.clody.presentation.utils.extension.handleApiResponse
import javax.inject.Inject

class DailyDiariesRepositoryImpl @Inject constructor(
    private val remoteDataSource: DailyDiariesDataSource
) : DailyDiariesRepository {
    override suspend fun getDailyDiariesData(year: Int, month: Int, date: Int): Result<DailyDiariesResponseDto> {
        return runCatching {
            remoteDataSource.getDailyDiariesData(year, month, date).handleApiResponse()
        }.getOrElse { exception ->
            Result.failure(exception)
        }
    }
}
