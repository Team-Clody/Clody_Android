package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.remote.datasource.DiaryRemoteDataSource
import com.sopt.clody.data.remote.dto.response.DailyDiariesResponseDto
import com.sopt.clody.data.remote.dto.response.DiaryTimeResponseDto
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import com.sopt.clody.data.remote.dto.response.MonthlyDiaryResponseDto
import com.sopt.clody.data.remote.dto.response.ReplyDiaryResponseDto
import com.sopt.clody.data.remote.dto.response.WriteDiaryResponseDto
import com.sopt.clody.data.remote.util.handleApiResponse
import com.sopt.clody.domain.repository.DiaryRepository
import com.sopt.clody.presentation.utils.network.ErrorMessages
import com.sopt.clody.presentation.utils.network.ErrorMessages.FAILURE_TEMPORARY_MESSAGE
import retrofit2.HttpException
import javax.inject.Inject

class DiaryRepositoryImpl @Inject constructor(
    private val diaryRemoteDataSource: DiaryRemoteDataSource
): DiaryRepository {
    override suspend fun writeDiary(date: String, content: List<String>): Result<WriteDiaryResponseDto> =
        runCatching {
            diaryRemoteDataSource.writeDiary(date, content).handleApiResponse().getOrThrow()
        }

    override suspend fun deleteDailyDiary(year: Int, month: Int, day: Int): Result<DailyDiariesResponseDto> =
        runCatching {
            diaryRemoteDataSource.deleteDailyDiary(year, month, day).handleApiResponse().getOrThrow()
        }

    override suspend fun getDailyDiariesData(year: Int, month: Int, date: Int): Result<DailyDiariesResponseDto> =
        runCatching {
            diaryRemoteDataSource.getDailyDiariesData(year, month, date).handleApiResponse()
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

    override suspend fun getDiaryTime(year: Int, month: Int, date: Int): Result<DiaryTimeResponseDto> =
        runCatching {
            diaryRemoteDataSource.getDiaryTime(year, month, date).data
        }

    override suspend fun getMonthlyCalendarData(year: Int, month: Int): Result<MonthlyCalendarResponseDto> =
        runCatching {
            diaryRemoteDataSource.getMonthlyCalendarData(year, month).handleApiResponse()
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

    override suspend fun getMonthlyDiary(year: Int, month: Int): Result<MonthlyDiaryResponseDto> =
        runCatching {
            diaryRemoteDataSource.getMonthlyDiary(year, month).handleApiResponse().getOrThrow()
        }

    override suspend fun getReplyDiary(year: Int, month: Int, date: Int): Result<ReplyDiaryResponseDto> =
        runCatching {
            val response = diaryRemoteDataSource.getReplyDiary(year, month, date).data
            if (response.content == null) {
                throw IllegalStateException(FAILURE_TEMPORARY_MESSAGE)
            }
            response
        }
}
