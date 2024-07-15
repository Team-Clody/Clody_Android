package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.remote.datasource.MonthlyCalendarDataSource
import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import com.sopt.clody.data.repository.MonthlyCalendarRepository
import javax.inject.Inject

class MonthlyCalendarRepositoryImpl @Inject constructor(
    private val remoteDataSource: MonthlyCalendarDataSource
) : MonthlyCalendarRepository {
    override suspend fun getMonthlyCalendarData(year: Int, month: Int): ApiResponse<List<MonthlyCalendarResponseDto>> {
        return remoteDataSource.getCalendarData(year, month)
    }
}
