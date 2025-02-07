package com.sopt.clody.data.remote.datasource

import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.response.DailyDiariesResponseDto
import com.sopt.clody.data.remote.dto.response.DiaryTimeResponseDto
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import com.sopt.clody.data.remote.dto.response.MonthlyDiaryResponseDto
import com.sopt.clody.data.remote.dto.response.ReplyDiaryResponseDto
import com.sopt.clody.data.remote.dto.response.WriteDiaryResponseDto

interface DiaryRemoteDataSource {
    suspend fun writeDiary(date: String, content: List<String>): ApiResponse<WriteDiaryResponseDto>
    suspend fun deleteDailyDiary(year: Int, month: Int, date: Int): ApiResponse<DailyDiariesResponseDto>
    suspend fun getDailyDiariesData(year: Int, month: Int, date:Int): ApiResponse<DailyDiariesResponseDto>
    suspend fun getDiaryTime(year: Int, month: Int, date: Int): ApiResponse<DiaryTimeResponseDto>
    suspend fun getMonthlyCalendarData(year: Int, month: Int): ApiResponse<MonthlyCalendarResponseDto>
    suspend fun getMonthlyDiary(year: Int, month: Int): ApiResponse<MonthlyDiaryResponseDto>
    suspend fun getReplyDiary(year: Int, month: Int, date: Int): ApiResponse<ReplyDiaryResponseDto>
}
