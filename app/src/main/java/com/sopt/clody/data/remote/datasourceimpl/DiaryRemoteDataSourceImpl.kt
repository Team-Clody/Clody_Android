package com.sopt.clody.data.remote.datasourceimpl

import com.sopt.clody.data.remote.api.DiaryService
import com.sopt.clody.data.remote.datasource.DiaryRemoteDataSource
import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.request.WriteDiaryRequestDto
import com.sopt.clody.data.remote.dto.response.DailyDiariesResponseDto
import com.sopt.clody.data.remote.dto.response.DiaryTimeResponseDto
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import com.sopt.clody.data.remote.dto.response.MonthlyDiaryResponseDto
import com.sopt.clody.data.remote.dto.response.ReplyDiaryResponseDto
import com.sopt.clody.data.remote.dto.response.WriteDiaryResponseDto
import javax.inject.Inject

class DiaryRemoteDataSourceImpl @Inject constructor(
    private val diaryService: DiaryService
): DiaryRemoteDataSource {
    override suspend fun writeDiary(date: String, content: List<String>): ApiResponse<WriteDiaryResponseDto> =
        diaryService.writeDiary(WriteDiaryRequestDto(date, content))

    override suspend fun deleteDailyDiary(year: Int, month: Int, date: Int): ApiResponse<DailyDiariesResponseDto> =
        diaryService.deleteDailyDiary(year = year, month = month, date = date)

    override suspend fun getDailyDiariesData(year: Int, month: Int, date: Int): ApiResponse<DailyDiariesResponseDto> =
        diaryService.getDailyDiariesData(year = year, month = month, date = date)

    override suspend fun getDiaryTime(year: Int, month: Int, date: Int): ApiResponse<DiaryTimeResponseDto> =
        diaryService.getDiaryTime(year = year, month = month, date = date)

    override suspend fun getMonthlyCalendarData(year: Int, month: Int): ApiResponse<MonthlyCalendarResponseDto> =
        diaryService.getMonthlyCalendarData(year = year, month = month)

    override suspend fun getMonthlyDiary(year: Int, month: Int): ApiResponse<MonthlyDiaryResponseDto> =
        diaryService.getMonthlyDiary(year = year, month = month)

    override suspend fun getReplyDiary(year: Int, month: Int, date: Int): ApiResponse<ReplyDiaryResponseDto> =
        diaryService.getReplyDiary(year = year, month = month, date = date)
}
