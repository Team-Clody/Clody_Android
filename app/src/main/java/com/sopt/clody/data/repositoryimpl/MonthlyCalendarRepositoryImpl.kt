package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.remote.datasource.MonthlyCalendarDataSource
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import com.sopt.clody.data.repository.MonthlyCalendarRepository
import com.sopt.clody.presentation.utils.extension.handleApiResponse
import javax.inject.Inject

class MonthlyCalendarRepositoryImpl @Inject constructor(
    private val remoteDataSource: MonthlyCalendarDataSource
) : MonthlyCalendarRepository {
    override suspend fun getMonthlyCalendarData(year: Int, month: Int): Result<MonthlyCalendarResponseDto> {
        return runCatching {
            remoteDataSource.getCalendarData(year, month).handleApiResponse()
        }.getOrElse { exception ->
            Result.failure(exception)
        }
    }
}


