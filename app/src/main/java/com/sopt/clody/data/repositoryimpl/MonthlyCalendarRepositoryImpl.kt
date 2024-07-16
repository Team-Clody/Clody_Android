package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.remote.datasource.MonthlyCalendarDataSource
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import com.sopt.clody.data.repository.MonthlyCalendarRepository
import javax.inject.Inject

class MonthlyCalendarRepositoryImpl @Inject constructor(
    private val remoteDataSource: MonthlyCalendarDataSource
) : MonthlyCalendarRepository {
    override suspend fun getMonthlyCalendarData(year: Int, month: Int): Result<MonthlyCalendarResponseDto> {
        return runCatching {
            val response = remoteDataSource.getCalendarData(year, month)
            if (response.status == 200 && response.data != null) {
                response.data
            } else {
                throw Exception(response.message)
            }
        }
    }
}
