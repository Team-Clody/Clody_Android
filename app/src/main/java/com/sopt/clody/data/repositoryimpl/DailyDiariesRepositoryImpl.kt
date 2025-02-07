package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.remote.datasource.DailyDiariesDataSource
import com.sopt.clody.data.remote.dto.response.DailyDiariesResponseDto
import com.sopt.clody.data.repository.DailyDiariesRepository
import com.sopt.clody.data.remote.util.handleApiResponse
import com.sopt.clody.presentation.utils.network.ErrorMessages
import retrofit2.HttpException
import javax.inject.Inject

class DailyDiariesRepositoryImpl @Inject constructor(
    private val remoteDataSource: DailyDiariesDataSource
) : DailyDiariesRepository {
    override suspend fun getDailyDiariesData(year: Int, month: Int, date: Int): Result<DailyDiariesResponseDto> {
        return runCatching {
            remoteDataSource.getDailyDiariesData(year, month, date).handleApiResponse()
        }.getOrElse { exception ->
            val errorMessage = when (exception) {
                is HttpException -> {
                    when (exception.code()) {
                        in 400..499 -> ErrorMessages.FAILURE_TEMPORARY_MESSAGE
                        in 500..599 -> ErrorMessages.FAILURE_SERVER_MESSAGE
                        else -> ErrorMessages.UNKNOWN_ERROR
                    }
                }
                else -> exception.message ?: ErrorMessages.UNKNOWN_ERROR
            }
            Result.failure(Exception(errorMessage))
        }
    }
}
