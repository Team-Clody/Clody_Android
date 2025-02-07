package com.sopt.clody.data.repository

import com.sopt.clody.data.remote.dto.response.DailyDiariesResponseDto
import com.sopt.clody.data.remote.dto.response.DiaryTimeResponseDto
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import com.sopt.clody.data.remote.dto.response.MonthlyDiaryResponseDto
import com.sopt.clody.data.remote.dto.response.ReplyDiaryResponseDto
import com.sopt.clody.data.remote.dto.response.WriteDiaryResponseDto

interface DiaryRepository {
    suspend fun writeDiary(date: String, content: List<String>): Result<WriteDiaryResponseDto>
    suspend fun deleteDailyDiary(year: Int, month: Int, day: Int): Result<DailyDiariesResponseDto>
    suspend fun getDailyDiariesData(year: Int, month: Int, date:Int): Result<DailyDiariesResponseDto>
    suspend fun getDiaryTime(year: Int, month: Int, date: Int): Result<DiaryTimeResponseDto>
    suspend fun getMonthlyCalendarData(year: Int, month: Int): Result<MonthlyCalendarResponseDto>
    suspend fun getMonthlyDiary(year: Int, month: Int): Result<MonthlyDiaryResponseDto>
    suspend fun getReplyDiary(year: Int, month: Int, date: Int): Result<ReplyDiaryResponseDto>
}
