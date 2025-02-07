package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import com.sopt.clody.data.repository.MonthlyCalendarRepository
import com.sopt.clody.data.remote.util.handleApiResponse
import com.sopt.clody.presentation.utils.network.ErrorMessages
import retrofit2.HttpException
import javax.inject.Inject

class MonthlyCalendarRepositoryImpl @Inject constructor(
    private val remoteDataSource: MonthlyCalendarDataSource
) : MonthlyCalendarRepository {
    override suspend fun getMonthlyCalendarData(year: Int, month: Int): Result<MonthlyCalendarResponseDto> {
        return runCatching {
            remoteDataSource.getCalendarData(year, month).handleApiResponse()
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
