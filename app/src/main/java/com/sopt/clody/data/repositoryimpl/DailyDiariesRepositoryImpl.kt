package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.remote.datasource.DailyDiariesDataSource
import com.sopt.clody.data.remote.dto.response.DailyDiariesResponseDto
import com.sopt.clody.data.repository.DailyDiariesRepository
import javax.inject.Inject

class DailyDiariesRepositoryImpl @Inject constructor(
    private val remoteDataSource: DailyDiariesDataSource
) : DailyDiariesRepository {
    override suspend fun getDailyDiariesData(year: Int, month: Int, date: Int): Result<DailyDiariesResponseDto> {
        return runCatching {
            val response = remoteDataSource.getDailyDiariesData(year, month, date)
            if (response.status == 200 && response.data != null) {
                response.data
            } else {
                throw Exception(response.message)
            }
        }
    }
}
